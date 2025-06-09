helm uninstall yabank
kubectl delete pvc data-yabank-postgresql-0
kubectl delete pvc data-yabank-kafka-broker-0
kubectl delete pvc data-yabank-kafka-controller-0
helm install yabank ./
