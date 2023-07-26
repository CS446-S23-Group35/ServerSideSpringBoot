
resource "aws_lb" "main" {
  name               = "${var.application_name}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = aws_security_group.load_balancer.*.id
  subnets            = [for subnet in aws_subnet.public_sub : subnet.id]
 
  enable_deletion_protection = false
}
 
resource "aws_alb_target_group" "main" {
  name        = "${var.application_name}-tg"
  port        = var.application_port
  protocol    = "HTTP"
  vpc_id      = aws_vpc.backend_vpc.id
  target_type = "ip"
 
  health_check {
   healthy_threshold   = "3"
   interval            = "30"
   protocol            = "HTTP"
   matcher             = "200"
   timeout             = "3"
   path                = "/heartbeat"
   unhealthy_threshold = "6"
  }
}

resource "aws_alb_listener" "https" {
  load_balancer_arn = aws_lb.main.id
  port              = 443
  protocol          = "HTTPS"
 
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.alb_tls_cert_arn
 
  default_action {
    target_group_arn = aws_alb_target_group.main.id
    type             = "forward"
  }
}

# resource "aws_alb_target_group" "search" {
#   name        = "${var.application_name}-search-tg"
#   port        = 9200
#   protocol    = "HTTP"
#   vpc_id      = aws_vpc.backend_vpc.id
#   target_type = "ip"
 
#   health_check {
#    healthy_threshold   = "3"
#    interval            = "30"
#    protocol            = "HTTPS"
#    matcher             = "200"
#    timeout             = "3"
#    path                = "/"
#    unhealthy_threshold = "2"
#   }
# }

# resource "aws_alb_listener" "search_http" {
#   load_balancer_arn = aws_lb.main.id
#   port              = 9200
#   protocol          = "HTTP"
 
#   ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
#   certificate_arn   = var.alb_search_cert_arn
 
#   default_action {
#     target_group_arn = aws_alb_target_group.search.id
#     type             = "forward"
#   }
# }
