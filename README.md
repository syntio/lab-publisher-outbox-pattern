# Dataphos Publisher as a replacement for the Transactional Outbox pattern

This repository demonstrates the Dataphos Publisher's ability to replace the transactional outbox pattern.

The transactional outbox pattern uses an outbox table (e.g. Postgres table) to store business event data.
For example, an Order service would split the order creation process in one insert in the Orders table,
and one insert in the Order Lines table. Then, the whole JSON (Order with OrderLines) would be inserted in the outbox
table to record the event with the context of the event (e.g., OrderCreated).

This event would then be extracted from the outbox table and published to a message broker (e.g., Apache Kafka) using a
CDC tool (e.g., Debezium). This event will then be asynchronously processed by one or more downstream consumers/services.
The outbox table is used to prevent inconsistencies in the data stored in the database and published to a message broker.
Databases offer strong transactional guarantees that message brokers cannot. Two writes to a database within a transaction
can easily be rolled back, but publishing data to a message broker cannot be rolled back after the database transaction
failed.

The Dataphos Publisher is a tool that ingests data from a database and creates business objects/domain events.
Meaning, we could skip the outbox table in this process and use the Publisher in real-time to track changes, form
domain events according to an event type and publish these events in order to Kafka.

### Workflow

For this purpose, a Spring application was created with routes for standard CRUD operations for Orders.
When the Spring App starts, tables Orders and Order Lines are created in the operational database (PostgreSQL in this 
case). HTTP requests can then be sent  via Postman in order to create, update or delete Order data in tables. 

3 Publisher instances are deployed: one that reads data based on the _'created_at'_ audit column, one that reads data
based on the _'updated_at'_ audit column, and one that reads data based on the _'deleted_at'_ audit column.
All three instances send messages to the same Kafka topic, since everything is related to the Order domain. The 
Publisher publishes these events to Kafka in order they were executed on the source database.

### Deployment

All services are deployed as Docker containers using Docker Compose.

The Orders application contains a Dockerfile that should be used to build a Docker image.

- Position yourself in the [orders-demo-app](/orders-demo-app) directory and run the following command.
  ```yaml
  docker build --rm -t orders-demo-app:latest .
  ```
  This will build the Docker image for the Spring application used in the [backend](/docker/backend.yaml) deployment
  file.
- Position yourself in the [docker](/docker) directory.
- Run the following command.
  ```yaml
  docker compose -f ./broker.yaml ./backend.yaml up -d
  ```
  This will start Apache Kafka and the Redpanda Console that can be used for monitoring. The Redpanda console is
  available at `http://localhost:8082`. This command will also start the Orders application and its PostgreSQL database.
  The Orders application is available on port `8089`. For example, the endpoint to create orders is exposed on
  the following URL: `http://localhost:8089/api/orders`. The PostgreSQL database is available on port `5433`.

The Dataphos Publisher deployment is split into multiple files, and it needs to be deployed in a specific order.

- Position yourself in the [publisher](/docker/publisher) directory within the [docker](/docker) directory.
- Run the following command 
  ```yaml
  docker compose -f ./infrastructure.yaml -f ./webui.yaml up -d
  ```
  This will deploy the Publisher's infrastructure components and it's Web UI used for configuration management and
  monitoring.
- Source, destination and then instances can now be added using the Web UI which can be accessed on
 `http://localhost:8085`. The instances must be added **after** the source and the destination. 
  On the Web UI open the `Web CLI` tab and drag-and-drop the [configuration](/configuration) files. First the
  [source](/configuration/source.yaml), then the [destination](configuration/destination.yaml), and finaly the three
  instances.
- After the instances are added, run the following commands to start the Worker components. That is, to start the 
  data ingestion jobs.
  ```yaml
  docker-compose -f <name of the worker.yaml file> up -d
  ```

### Demonstration

The Orders can be created, updated and deleted via HTTP requests. For example, `Postman` or `curl` can be used.  

#### Creating an Order

Send the following JSON body using a `POST` request to the POST endpoint: `http://localhost:8089/api/orders`:

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

#### Updating an Order

Send the following JSON body using a `PUT` request to the PUT endpoint for the previously added order:
`http://localhost:8089/api/orders/1`:

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

#### Deleting an Order

When deleting an order, the row isn't actually removed from the database. The column `is_active` is just switched to
_false_, and the `deleted_at` column is updated to the current timestamp. This way the Publisher can extract the deleted
order and store it on the Kafka topic with the event context.

Send a `DELETE` request to the DELETE endpoint for the previously added and updated order:
`http://localhost:8089/api/orders/1`.


### Monitoring

The Publisher should fetch the data from the database and send it to the defined Kafka topic in the right order.
To be able to visually track the messages, you can check the Redpanda Console Kafka UI, 
available on `http://localhost:8082`.

### Cleaning up

To stop the demonstration, all Docker containers must be stopped.

- Position yourself in the [publisher](/docker/publisher) directory and run the following commands.
  ```yaml
  docker compose -f <name of the worker.yaml file> down
  ```
  This will stop all your running data ingestion jobs.
- Position yourself in the [docker](/docker) directory and run the following commands.
  ```yaml
  docker compose -f ./infrastructure.yaml -f ./webui.yaml down --volumes 
  docker compose -f ./broker.yaml -f ./backend.yaml down --volumes
  ```
  The first command will remove all the Publisher's infrastructure and the Web UI. The second command will remove all
  Kafka-related resources and the Orders application. All related data volumes will be removed as well.
