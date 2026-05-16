# Forum Service v2

Spring Boot 3 REST API for a vehicle-based discussion forum. Users can create threads organized by vehicle brand, open subthreads for specific topics, post messages, and vote on them.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Persistence | Spring Data JPA + PostgreSQL 16 |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Utilities | Lombok |
| Build | Maven |
| Container | Docker + Docker Compose |

---

## Data Model

```
VehicleBrand
  └── Thread (brand-level discussion)
        └── Subthread (topic within a thread)
              └── Message
                    └── MessageVote (upvote / downvote)

Vehicle          — standalone vehicle entries
Topic            — generic discussion topics
  ├── AttributeList
  └── Comment
```

---

## Project Structure

```
src/main/java/com/forum/
├── ForumServiceApplication.java
├── controller/
│   ├── ForumController.java         # threads, subthreads, messages, votes
│   ├── VehicleAdminController.java  # admin: brand management
│   ├── VehicleController.java       # vehicle CRUD
│   └── TopicController.java         # topics, attributes, comments
├── service/
│   ├── ForumService.java
│   ├── VehicleAdminService.java
│   ├── VehicleService.java
│   └── TopicService.java
├── repository/
│   ├── ThreadRepository.java
│   ├── SubthreadRepository.java
│   ├── MessageRepository.java
│   ├── MessageVoteRepository.java
│   ├── VehicleBrandRepository.java
│   ├── VehicleRepository.java
│   ├── TopicRepository.java
│   ├── AttributeListRepository.java
│   └── CommentRepository.java
├── entity/
│   ├── ForumThread.java
│   ├── Subthread.java
│   ├── Message.java
│   ├── MessageVote.java
│   ├── MessageVoteId.java
│   ├── VehicleBrand.java
│   ├── Vehicle.java
│   ├── Topic.java
│   ├── AttributeList.java
│   └── Comment.java
└── dto/
    ├── CreateThreadRequest.java / ThreadResponse.java
    ├── CreateSubthreadRequest.java / SubthreadResponse.java
    ├── CreateMessageRequest.java / MessageResponse.java
    ├── VoteRequest.java
    ├── CreateVehicleBrandRequest.java / VehicleBrandResponse.java
    ├── CreateVehicleRequest.java / VehicleResponse.java
    ├── CreateTopicRequest.java / TopicResponse.java
    ├── CreateAttributeRequest.java / AttributeResponse.java
    └── CreateCommentRequest.java / CommentResponse.java
```

---

## Configuration

`src/main/resources/application.yml` — environment variables with defaults:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/forum}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
```

The schema is initialised automatically from `schema.sql` on every startup (`spring.sql.init.mode: always`). Hibernate is set to `create-drop`, so the DB is recreated on each run. Seed data (vehicle brands, sample threads/messages) is included in `schema.sql`.

---

## Running Locally

### With Docker Compose (recommended)

```bash
docker compose up --build
```

This starts:
- `postgres` container on port `5432` (database: `forum_db`)
- `app` container on port `8080`

### Without Docker

1. Start a local PostgreSQL instance with database `forum`.
2. Build and run:

```bash
mvn clean package -DskipTests
java -jar target/forum-servicev2-1.0.0.jar
```

The service starts on **http://localhost:8080**.

Swagger UI is available at **http://localhost:8080/swagger-ui.html**.

---

## API Reference

### Admin — Vehicle Brands

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/admin/brands` | Create a vehicle brand |
| `GET` | `/admin/brands` | List all brands |

**POST `/admin/brands`**
```json
{
  "name": "BMW"
}
```

---

### Vehicles

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/vehicles` | List all vehicles |
| `POST` | `/vehicles` | Create a vehicle |

**POST `/vehicles`**
```json
{
  "brandId": "uuid-of-brand",
  "model": "M3",
  "year": 2021
}
```

---

### Threads

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/threads` | Create a thread |
| `GET` | `/threads?page=0&size=10` | List all threads (paginated) |
| `GET` | `/threads/recent?limit=10` | List recent threads |
| `GET` | `/threads/{threadId}/subthreads` | List subthreads of a thread |
| `GET` | `/threads/{threadId}/subthreads?includeMessages=true` | Include messages in response |

**POST `/threads`**
```json
{
  "userId": "user123",
  "type": "car-discussion",
  "vehicleBrandId": "uuid-of-brand",
  "title": "BMW General Discussion"
}
```

---

### Subthreads

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/threads/{threadId}/subthreads` | Create a subthread |
| `GET` | `/subthreads?page=0&size=10` | List all subthreads (paginated) |
| `GET` | `/subthreads/recent?limit=10` | List recent subthreads |

**POST `/threads/{threadId}/subthreads`**
```json
{
  "userId": "user123",
  "title": "Engine Vibration Issue",
  "initialMessage": "I have been experiencing engine vibration on my 2021 M3..."
}
```

---

### Messages

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/subthreads/{subthreadId}/messages` | Post a message |
| `GET` | `/subthreads/{subthreadId}/messages` | List messages in a subthread |

**POST `/subthreads/{subthreadId}/messages`**
```json
{
  "userId": "user123",
  "body": "I experienced the same issue."
}
```

---

### Votes

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/messages/{messageId}/vote` | Upvote or downvote a message |

**POST `/messages/{messageId}/vote`**
```json
{
  "userId": "user123",
  "upvoted": true
}
```

---

### Topics

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/topics` | List all topics |
| `POST` | `/topics` | Create a topic |
| `GET` | `/topics/{topicId}/attributes` | List attributes of a topic |
| `POST` | `/topics/{topicId}/attributes` | Add an attribute to a topic |
| `GET` | `/topics/{topicId}/comments` | List comments on a topic |
| `POST` | `/topics/{topicId}/comments` | Add a comment to a topic |

---

## Database

PostgreSQL with UUID primary keys (`pgcrypto` extension). Tables:

- `threads` — brand-level forum threads
- `subthreads` — topic threads within a forum thread
- `messages_table` — user messages in a subthread
- `message_vote` — composite PK `(message_id, user_id)`, stores upvote/downvote
- `vehicle_brands` — pre-seeded with 30 brands (Toyota, BMW, Tesla, TOGG, ...)

---

## License

MIT
