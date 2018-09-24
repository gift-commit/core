pip install --user awscli
export PATH=$PATH:$HOME/.local/bin
eval $(aws ecr get-login --region us-west-2 --no-include-email)
docker tag gift-commit/core:latest 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:$TRAVIS_TAG
docker push 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:$TRAVIS_TAG

ssh -i "secrets/decrypted/shared.pem" ec2-user@ec2-35-163-156-14.us-west-2.compute.amazonaws.com << EOF
   eval $(aws ecr get-login --region us-west-2 --no-include-email)
   docker pull 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest
   docker stop core && docker rm core
   docker run --restart=always --name core -p 8080:8080 -d 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest
EOF