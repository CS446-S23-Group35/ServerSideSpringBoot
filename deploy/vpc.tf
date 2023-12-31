resource "aws_vpc" "backend_vpc" {
 cidr_block = var.vpc_cidr
 enable_dns_hostnames = true
 
 tags = {
   Name = "VPC"
 }
}

###########
# Subnets #
###########

resource "aws_subnet" "public_sub" {
  count = length(var.aws_availability_zones)
  vpc_id     = aws_vpc.backend_vpc.id
  cidr_block = var.public_subnet_cidrs[count.index]

  availability_zone = var.aws_availability_zones[count.index]
  
  tags = {
    Name = "Public Subnet"
  }
}

resource "aws_subnet" "private_sub" {
  count = length(var.aws_availability_zones)
  vpc_id     = aws_vpc.backend_vpc.id
  cidr_block = var.private_subnet_cidrs[count.index]

  availability_zone = var.aws_availability_zones[count.index]
  
  tags = {
    Name = "Private Subnet"
  }
}

###########################
# Gateway and Route Table #
###########################

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.backend_vpc.id
 
  tags = {
    Name = "VPC Internet Gateway"
  }
}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.backend_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "Internet Route Table"
  }
}

resource "aws_route_table_association" "public_subnet_asso" {
  count = length(var.aws_availability_zones)
  subnet_id      = aws_subnet.public_sub[count.index].id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_eip" "gw" {
  count      = length(var.aws_availability_zones)
  vpc        = true
  depends_on = [aws_internet_gateway.gw]
}

resource "aws_nat_gateway" "gw" {
  count         = length(var.aws_availability_zones)
  subnet_id     = aws_subnet.public_sub[count.index].id
  allocation_id = aws_eip.gw[count.index].id
}

resource "aws_route_table" "private_rt" {
  count = length(var.aws_availability_zones)
  vpc_id = aws_vpc.backend_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    # nat_gateway_id = aws_nat_gateway.gw[count.index].id
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "Private Route Table"
  }
}

resource "aws_route_table_association" "private" {
  count          = length(var.aws_availability_zones)
  subnet_id      = element(aws_subnet.private_sub.*.id, count.index)
  route_table_id = element(aws_route_table.private_rt.*.id, count.index)
}

###################
# Security Groups #
###################

resource "aws_security_group" "bastion_sg" {
  name        = "Bastion Security Group"
  description = "Bastion Security Group"
  vpc_id      = aws_vpc.backend_vpc.id

  ingress {
    description = "SSH from VPC"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "user_db" {
  name        = "User DB Security Group"
  description = "User DB Security Group"
  vpc_id      = aws_vpc.backend_vpc.id

  ingress {
    description = "DB Connection"
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "load_balancer" {
  name        = "Load Balancer Security Group"
  vpc_id      = aws_vpc.backend_vpc.id

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
   protocol         = "tcp"
   from_port        = 443
   to_port          = 443
   cidr_blocks      = ["0.0.0.0/0"]
   ipv6_cidr_blocks = ["::/0"]
  }
 
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "application_sb" {
  name        = "Application Security Group"
  vpc_id      = aws_vpc.backend_vpc.id

  ingress {
    protocol    = "tcp"
    from_port   = var.application_port
    to_port     = var.application_port
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 80
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "search" {
  name        = "Search Security Group"
  vpc_id      = aws_vpc.backend_vpc.id

  ingress {
    protocol    = "tcp"
    from_port   = 9200
    to_port     = 9200
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 9600
    to_port     = 9600
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
