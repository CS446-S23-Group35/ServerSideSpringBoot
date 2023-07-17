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

resource "aws_db_subnet_group" "user_db" {
  name       = "user-db-subnet-group"
  subnet_ids = [aws_subnet.private_sub[1].id, aws_subnet.private_sub[2].id]

  tags = {
    Name = "DB subnet group"
  }
}
