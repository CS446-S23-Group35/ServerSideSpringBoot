resource "aws_ecs_cluster" "application" {
  name = "${var.application_name}-cluster"
}

resource "aws_ecs_task_definition" "application" {
  family = "${var.application_name}-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn
  container_definitions = templatefile("${path.module}/ecs_definitions/application.json", 
    {
      application_name = var.application_name
      container_image = var.application_image
      container_port = var.application_port
      db_url = format("jdbc:postgresql://%s/postgres", aws_db_instance.user_db.address)
      db_username = aws_db_instance.user_db.username
      db_password = aws_db_instance.user_db.password
    }
  )
}
 
resource "aws_iam_role" "ecs_task_role" {
  name = "${var.application_name}-ecsTaskRole"
 
  assume_role_policy = <<EOF
{
 "Version": "2012-10-17",
 "Statement": [
   {
     "Action": "sts:AssumeRole",
     "Principal": {
       "Service": "ecs-tasks.amazonaws.com"
     },
     "Effect": "Allow",
     "Sid": ""
   }
 ]
}
EOF
}

resource "aws_iam_policy" "application_access" {
  name        = "${var.application_name}_access_policy"
  description = "Policy that allows the necessary access for the application"
 
 policy = <<EOF
{
   "Version": "2012-10-17",
   "Statement": [
       {
           "Effect": "Allow",
           "Action": [
                "rds-db:connect"
           ],
           "Resource": "*"
       }
   ]
}
EOF
}
 
resource "aws_iam_role_policy_attachment" "ecs-task-role-policy-attachment" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.application_access.arn
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.application_name}-ecsTaskExecutionRole"
 
  assume_role_policy = <<EOF
{
 "Version": "2012-10-17",
 "Statement": [
   {
     "Action": "sts:AssumeRole",
     "Principal": {
       "Service": "ecs-tasks.amazonaws.com"
     },
     "Effect": "Allow",
     "Sid": ""
   }
 ]
}
EOF
}
 
resource "aws_iam_role_policy_attachment" "ecs-task-execution-role-policy-attachment" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "ecs-task-execution-role-image-pull" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.image_pull.arn
}

resource "aws_iam_policy" image_pull {
  name        = "${var.application_name}_image_pull"
  description = "Policy that allows ecs to pull the image from ECR"
 
 policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "ecr:DescribeImageScanFindings",
                "ecr:GetLifecyclePolicyPreview",
                "ecr:GetDownloadUrlForLayer",
                "ecr:DescribeImageReplicationStatus",
                "ecr:ListTagsForResource",
                "ecr:ListImages",
                "ecr:BatchGetRepositoryScanningConfiguration",
                "ecr:BatchGetImage",
                "ecr:DescribeImages",
                "ecr:DescribeRepositories",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetRepositoryPolicy",
                "ecr:GetLifecyclePolicy"
            ],
            "Resource": "arn:aws:ecr:*:068734148537:repository/*"
        },
        {
            "Sid": "VisualEditor1",
            "Effect": "Allow",
            "Action": [
                "ecr:GetRegistryPolicy",
                "ecr:DescribeRegistry",
                "ecr:GetAuthorizationToken",
                "ecr:GetRegistryScanningConfiguration"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}

resource "aws_ecs_service" "main" {
  name                               = "${var.application_name}-service"
  cluster                            = aws_ecs_cluster.application.id
  task_definition                    = aws_ecs_task_definition.application.arn
  desired_count                      = 1
  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  launch_type                        = "FARGATE"
  scheduling_strategy                = "REPLICA"
 
  network_configuration {
    security_groups  = aws_security_group.application_sb.*.id
    subnets          = [for subnet in aws_subnet.public_sub : subnet.id]
    assign_public_ip = true
  }
 
  load_balancer {
    target_group_arn = aws_alb_target_group.main.arn
    container_name   = "${var.application_name}-container"
    container_port   = var.application_port
  }
 
  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }
}
