## Kafka Topic 개수 확인
```bash
docker exec -it broker-1 /opt/kafka/bin/kafka-get-offsets.sh \
  --bootstrap-server broker-1:9092 \
  --topic TicketCreated \
  --time -1
```
TicketCreated:0:10
