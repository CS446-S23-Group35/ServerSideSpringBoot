output "rds_hostname" {
  description = "User RDS instance hostname"
  value       = aws_db_instance.user_db.address
  sensitive   = true
}

output "rds_port" {
  description = "User RDS instance port"
  value       = aws_db_instance.user_db.port
  sensitive   = true
}

output "rds_username" {
  description = "User RDS instance root username"
  value       = aws_db_instance.user_db.username
  sensitive   = true
}

output "cognito_user_pool_id" {
  description = "Cognito User Pool ID"
  value       = aws_cognito_user_pool.pool.id
}
