ssh -i "~/.ssh/shared.pem" ec2-user@ec2-35-163-156-14.us-west-2.compute.amazonaws.com << EOF
   docker pull 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest
   docker stop core && docker rm core
   docker run --restart=always --name core -p 8080:8080 -d 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest
EOF