# Small EC2 instance to run the setup script and to work as a bastion host
resource "aws_instance" "bastion" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"
  
  key_name      = var.bastion_key_pair
  subnet_id = aws_subnet.public_sub[1].id
  vpc_security_group_ids = [aws_security_group.bastion_sg.id]
  associate_public_ip_address = true

  tags = {
    Name = "bastion"
  }
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

# User Database - Postgres
resource "aws_db_instance" "user_db" {
  identifier             = "user-db"
  instance_class         = "db.t3.micro"
  allocated_storage      = 5
  engine                 = "postgres"
  engine_version         = "15.3"
  db_name                = "user_db"
  username               = "cs446"
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.user_db.name
  vpc_security_group_ids = [aws_security_group.user_db.id]
  parameter_group_name   = aws_db_parameter_group.user_db.name
  publicly_accessible    = false
  skip_final_snapshot    = true
}

resource "aws_db_parameter_group" "user_db" {
  name   = "user-db"
  family = "postgres15"

  parameter {
    name  = "log_connections"
    value = "1"
  }
}


# Vespa ECS

# gRPC Image
