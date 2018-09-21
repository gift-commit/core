#!/bin/sh

pip install --user awscli
export PATH=$PATH:$HOME/.local/bin
eval $(aws ecr get-login --region us-west-2)
docker tag gift-commit/core:latest 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest
docker push 046006883301.dkr.ecr.us-west-2.amazonaws.com/gift-commit/core:latest