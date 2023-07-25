resource "aws_ecs_task_definition" "search" {
  family = "${var.application_name}-search-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 512
  memory                   = 2048
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn
  container_definitions = templatefile("${path.module}/ecs_definitions/opensearch.json", 
    {
      application_name = var.application_name
    }
  )
}

resource "aws_ecs_service" "search" {
  name                               = "${var.application_name}-search-service"
  cluster                            = aws_ecs_cluster.application.id
  task_definition                    = aws_ecs_task_definition.search.arn
  desired_count                      = 1
  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  launch_type                        = "FARGATE"
  scheduling_strategy                = "REPLICA"
 
  network_configuration {
    security_groups  = aws_security_group.search.*.id
    subnets          = [for subnet in aws_subnet.public_sub : subnet.id]
    assign_public_ip = true
  }
 
#   load_balancer {
#     target_group_arn = aws_alb_target_group.search.id
#     container_name   = "${var.application_name}-search"
#     container_port   = 9200
#   }
 
  lifecycle {
    ignore_changes = [desired_count]
  }
}
