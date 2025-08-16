# nearby-friends-service

A microservices-based **Nearby Friends** application that allows users to see how many of their friends are nearby in real-time.
It leverages **WebSockets**, **Redis Pub/Sub**, and **Docker** to deliver scalable, low-latency updates.

---

## 🚀 Features

* **Real-time Location Updates**: Users connect via WebSocket and send location updates.
* **Proximity Detection**: A proximity-service calculates nearby friends within a radius.
* **Redis Pub/Sub**: Location updates are broadcasted across services for scalability.
* **Redis Persistence (Planned)**: Store locations durably for crash recovery.
* **Authentication (Planned)**: Secure WebSocket connections with JWT.
* **Horizontal Scaling (Planned)**: Multiple WebSocket servers connected through Redis.

---

## 📦 Tech Stack

* **Java 17 / Spring Boot 3**
* **WebSocket** (real-time communication)
* **Redis** (Pub/Sub + persistence)
* **Docker / Docker Compose**
* **Maven**

---

## 🔧 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/nearby-friends-service.git
cd nearby-friends-service
```

### 2. Build services

```bash
mvn clean package -DskipTests
```

### 3. Start Docker stack

```bash
docker-compose up --build
```

---

## 📡 Usage

### Connect via WebSocket

```ws
ws://localhost:8080/ws/location
```

Send JSON messages:

```json
{
  "userId": "bob",
  "latitude": 12.9716,
  "longitude": 77.5946
}
```

### Query Proximity

```http
GET http://localhost:8081/proximity/nearby?userId=bob&radiusKm=1
```

Response:

```json
[
  { "userId": "alice", "latitude": 12.9721, "longitude": 77.5938 }
]
```

---

## 📌 Roadmap

* [ ] Add Redis persistence (RDB/AOF)
* [ ] Secure WebSocket connections with JWT authentication
* [ ] Support multiple WebSocket servers (clustered scaling)
* [ ] Add integration tests

---

## 📜 License

The project is licensed under the terms of GNU Lesser General Public License v2.1 (/LICENSE)

