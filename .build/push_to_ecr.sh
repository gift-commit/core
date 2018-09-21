#!/bin/sh

pip install --user awscli
export PATH=$PATH:$HOME/.local/bin
eval $(aws ecr get-login --region us-east-1)
docker build -t gift-commit/core .build/
docker tag gift-commit/core:latest 046006883301.dkr.ecr.us-east-1.amazonaws.com/gift-commit/core:latest
docker push 046006883301.dkr.ecr.us-east-1.amazonaws.com/gift-commit/core:latest