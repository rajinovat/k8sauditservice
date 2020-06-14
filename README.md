=========Steps to build and release the docker image for auditimageservice======

cd /Users/rajesh.ramasamy/Downloads/java-projects/demo

./gradlew build && java -jar build/libs/auditimageservice-0.1.0.jar

docker build -t rajinovat/auditimageservice:0.1.0 .

docker login <enter>

username: user

password: #########

docker push rajinovat/auditimageservice:0.1.0

kind create cluster
kubectl create sa tiller -n kube-system
kubectl create clusterrolebinding tiller-cluster-rule --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
 
	
helm init --service-account tiller --override spec.selector.matchLabels.'name'='tiller',spec.selector.matchLabels.'app'='helm' --output yaml | sed 's@apiVersion: extensions/v1beta1@apiVersion: apps/v1@' | kubectl create  -f -


kubectl create clusterrolebinding audit-cluster-rule --clusterrole=cluster-admin --serviceaccount=audit:default



cd chart/auditimageservice

helm delete audit --purge 

helm install --name audit  --namespace audit .
  or
  
  helm upgrade  audit  .

how to test without ingress

 kubectl exec -it tiller-deploy-77855d9dcf-kxzh4 sh -n kube-system

 cd /tmp
 rm index.html

wget http://audit-auditimageservice.audit.svc.cluster.local


 kubectl logs audit-auditimageservice-8647d99fcf-69p  -n audit 



  kubectl get pods -n audit    


helm upgrade audit  --namespace audit .
