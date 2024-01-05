# Publisher-outbox-pattern

In this repository Publisher is used as a replacement for the transactional outbox pattern.
The transactional outbox pattern uses an outbox table (e.g. Postgres table) to store business event data.
For example, an Order service would split the order creation process in one insert in the Orders table,
and one insert in the Order Lines table. Then, the whole JSON (Order with OrderLines) would be inserted in the outbox
table to record the event.
This event would then be extracted from the outbox table and published to Kafka using Debezium to track CDC changes.

The Dataphos Publisher is used to create business objects at the source. Meaning, we could skip
the outbox table in this process and use the Publisher in real-time to track changes and publish events to Kafka.

### Workflow

For this purpose, a Spring REST API is created with routes for standard CRUD operations for Orders. When the Spring Boot
App starts, tables Orders and
Order Lines are created in the operational database (Docker Postgres). HTTP requests can then be sent
via Postman in order to create, update or delete Order data in tables. Also, 3 Publisher instances are deployed in
Docker: one that reads data based on
_'created_at'_ audit column and has "OrderCreated" defined as the event type in metadata, one that reads data based
on _'updated_at'_ audit column and has
"OrderUpdated" defined as the event type in metadata and one that reads data based on _'deleted_at'_ audit column and
has "OrderDeleted" defined as the event type in metadata.
All three instances send messages to the same Kafka topic, since everything is related to the Order domain. Publisher
publishes these events to Kafka in order they
were executed on the source database.

### Running Spring Boot Application and Publisher

- First go to docker &rarr; database folder and run `docker-compose -f postgres.yaml up -d` in order to start the
  database that will store order records.
- Run PublisherOutboxApplication. It is located in publisher-outbox-pattern &rarr; src &rarr; main &rarr; java folder.
- Go to docker &rarr; kafka folder and run `docker-compose -f kafka.yaml up -d` to start Kafka.
- Run the Publisher: go to docker &rarr; publisher folder and run `docker-compose -f infrastructure.yaml up -d`
  and `docker-compose -f webui.yaml up -d`.
  Source, destination and instances can now be added using WebUI which can be accessed on [http://localhost:8085]().
  Needed .yaml files are located in demo &rarr; publisher folder.
- After the instances are added, run `docker-compose -f <name of the worker.yaml file> up` to start the Worker
  component.

Now the Orders can be created via Postman POST requests.  
Creating an Order - example of a message body in POST request to [http://localhost:8089/api/orders]():

```json
{
  "purchaser": "Leon",
  "paymentMethod": "card",
  "orderLines": [
    {
      "product": "jeans",
      "quantity": 1,
      "price": 80.00
    },
    {
      "product": "sneakers",
      "quantity": 1,
      "price": 70.00
    }
  ]
}
```

Updating an Order - example of a message body in PUT request to [http://localhost:8089/api/orders/1]():

```json
{
  "orderId": 1,
  "purchaser": "Tea",
  "paymentMethod": "cash",
  "orderLines": [
    {
      "id": 1,
      "product": "jacket",
      "quantity": 2,
      "price": 80.00
    },
    {
      "id": 2,
      "product": "sneakers",
      "quantity": 1,
      "price": 70.00
    }
  ]
}
```

When deleting an order, the row isn't actually removed from the database, column 'is_active' is just switched to _false_
.

Publisher should fetch the data from the database and send it to the defined Kafka topic in the right order. To be able
to visually track the messages, you can reach the Redpanda Console Kafka UI, available on [http://localhost:8082]().