## Kafka Topic 개수 확인
```bash
docker exec -it broker-1 \
kafka-run-class kafka.tools.GetOffsetShell \            
  --broker-list localhost:9092 \
  --topic TicketCreated \
  --time -1
```
TicketCreated:0:5
