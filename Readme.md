# Setup
## Software
1. JDK 17
2. Docker & docker compose
3. IntelliJ
4. Mongo, Postgres etc - use docker
5. DBeaver - Postgres Client
6. Robo3t Studio - Mongo Client
7. Kafka and Zookeeper

## Start Infrastructure
> Make it sure no local databases (mongo, postgres, zookeeper and kafka) are running
```shell
docker-compose up -d
```
This will start Mongo, Postgres, Kafka and Zookeeper.
> Test connection using Robo3T and DBeaver
### Robo3T
* Host : localhost
* Port : 27017
### Postgres
* Host : localhost
* Port : 5432
* Database : postgres
* User : postgres
* Password : postgres
### Kafka
Check http://localhost:8080/ 

One of the broker should be available
## Stop Infrastructure
```shell
docker-compose down
```