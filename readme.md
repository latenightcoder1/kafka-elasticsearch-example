command to make elasticsearch container up-

docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" -e "
xpack.security.http.ssl.enabled=false" -e "xpack.security.enabled=true" -e "
ELASTIC_PASSWORD=elastic" elasticsearch:8.9.0

other way to set up password-
docker exec -it elasticsearch bash
elasticsearch-setup-passwords interactive (to set up password for all default users)

command to make kafka and zookeper container up-

docker compose -f
/Users/surajkumar/Documents/personal_workspace/kafka-elasticsearch-example/docker-compose.yml up -d

Remaining Items-
a) custom object deserialization while receiving from kafka and writing to elasticsearch

to get into kafka container to see written messages

docker exec -it kafka-elasticsearch-example-kafka-1 kafka-console-consumer.sh --bootstrap-server
localhost:9092 --topic my_topic3 --from-beginning
