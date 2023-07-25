# Small EC2 instance to run the setup script and to work as a bastion host
resource "aws_instance" "bastion" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = "t3.micro"
  
  key_name      = var.bastion_key_pair
  subnet_id = aws_subnet.public_sub[1].id
  vpc_security_group_ids = [aws_security_group.bastion_sg.id]
  associate_public_ip_address = true
  iam_instance_profile = aws_iam_instance_profile.bastion.name

  tags = {
    Name = "bastion"
  }
  
  lifecycle {
    ignore_changes = [iam_instance_profile, ami]
  }
}

data "aws_ami" "amazon_linux" {
  most_recent = true


 filter {
   name   = "owner-alias"
   values = ["amazon"]
 }


 filter {
   name   = "name"
   values = ["al2023-ami*"]
 }
}

resource "aws_iam_role" "bastion_role" {
  name = "${var.application_name}-bastionRole"
 
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

resource "aws_iam_role_policy_attachment" "bastion-image-pull" {
  role       = aws_iam_role.bastion_role.name
  policy_arn = aws_iam_policy.image_pull.arn
}

resource "aws_iam_instance_profile" "bastion" {
  name = "${var.application_name}-bastion"
  role = aws_iam_role.bastion_role.name
}
