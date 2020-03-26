sh docker/build.sh

terraform taint kubernetes_deployment.fa-app
docker push tisserv/fa-app
terraform apply