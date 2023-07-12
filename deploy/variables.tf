variable "aws_profile" {
  description = "AWS profile to use"
  default     = "default"
}

variable "aws_region" {
  type        = string
  description = "AWS region to use"
  default     = "us-east-2"
}

variable "aws_availability_zones" {
  type        = list(string)
  description = "AWS availability zones to use. Must have at least 2. Must match the region."
  default     = ["us-east-2a", "us-east-2b", "us-east-2c"]
}

variable "vpc_cidr" {
  type        = string
  description = "VPC CIDR value"
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  type        = list(string)
  description = "Public Subnet CIDR values. Must have one for each AZ."
  default     = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}
 
variable "private_subnet_cidrs" {
  type        = list(string)
  description = "Private Subnet CIDR values. Must have one for each AZ. "
  default     = ["10.0.4.0/24", "10.0.5.0/24", "10.0.6.0/24"]
}

variable "bastion_key_pair" {
  type        = string
  description = "Bastion key pair name"
}

variable "db_password" {
  type        = string
  description = "Password for the user database"
  sensitive   = true
}
