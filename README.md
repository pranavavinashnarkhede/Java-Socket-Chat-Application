# Java Multithreaded Socket-Based Group Chat Application

A simple **client-server chat application built using Java Socket Programming and Multithreading**. Multiple clients can connect to a central server and exchange messages through group chat broadcasting.

## Features

* **TCP Socket Communication:** Establishes communication between clients and the server using Java sockets.
* **Multithreaded Server:** Handles multiple client connections simultaneously by creating a separate thread for each client.
* **Group Chat:** Allows connected clients to exchange messages through the server.
* **Message Broadcasting:** Broadcasts a client's message to all connected clients.
* **Independent Message Receiving:** Clients can receive messages while typing their own messages.
* **Client Disconnection Handling:** Handles client disconnections and removes disconnected clients from the server.
* **Input Validation:** Rejects empty or whitespace-only messages.
* **Graceful Disconnection:** Clients can type `end` to disconnect from the chat.

## Technologies Used

* **Programming Language:** Java
* **Networking:** TCP Socket Programming
* **Multithreading:** `Thread` and `Runnable`
* **Input/Output:** `BufferedReader`, `InputStreamReader`, and `PrintWriter`
* **Concurrent Collection:** `ConcurrentHashMap`
* **Development Environment:** VS Code or any Java-compatible IDE

## Project Structure

```text
Java-Socket-Chat-Application/
│
├── src/
│   ├── Server.java
│   ├── ClientHandler.java
│   └── Client.java
│
├── README.md
└── .gitignore
```

### File Descriptions

| File                 | Description                                                                       |
| -------------------- | --------------------------------------------------------------------------------- |
| `Server.java`        | Starts the server, accepts client connections, and manages broadcasting.          |
| `ClientHandler.java` | Handles individual client communication using a separate thread.                  |
| `Client.java`        | Connects to the server, sends messages, and receives messages from other clients. |

## How It Works

1. The server starts and listens for client connections on port `2101`.
2. Clients connect to the server using TCP sockets.
3. The server creates a separate `ClientHandler` and thread for each connected client.
4. When a client sends a message, the server receives it through that client's handler.
5. The server broadcasts the message to the connected clients.
6. Each client receives messages independently while remaining able to type new messages.
7. When a client disconnects, the server removes it from the collection of connected clients.

### Architecture

```text
                +------------------+
                |   Chat Server    |
                |   Port: 2101     |
                +------------------+
                         |
              +----------+----------+
              |          |          |
         +---------+ +---------+ +---------+
         | Client 1| | Client 2| | Client 3|
         +---------+ +---------+ +---------+
              |          |          |
         +---------+ +---------+ +---------+
         | Handler | | Handler | | Handler |
         | Thread 1| | Thread 2| | Thread 3|
         +---------+ +---------+ +---------+
              |          |          |
              +----------+----------+
                         |
                  Message Broadcasting
```

## Prerequisites

* Java Development Kit (JDK) installed.
* Java compiler (`javac`) and Java runtime (`java`) available in the system PATH.
* Terminal, Command Prompt, or VS Code.

Verify your Java installation:

```bash
java -version
javac -version
```

## How to Run

Run these commands from the project root directory.

### Step 1: Compile the Project

```bash
javac -d out src/*.java
```

This compiles the Java source files and places the generated class files inside the `out` directory.

### Step 2: Start the Server

```bash
java -cp out Server
```

Expected output:

```text
Chat Server Started...
Waiting for clients on port 2101
```

Keep this terminal running.

### Step 3: Start the First Client

Open another terminal and run:

```bash
java -cp out Client
```

### Step 4: Start Additional Clients

Open separate terminals for additional clients and run:

```bash
java -cp out Client
```

Each client connects to the same server.

### Step 5: Exchange Messages

Type a message in any connected client and press Enter.

The server broadcasts the message to the connected clients.

Example:

```text
Enter Message: Hello everyone
Client 50408: Hello everyone
```

The exact client port number will vary.

### Step 6: Disconnect a Client

Type:

```text
end
```

Then press Enter to disconnect that client.

To stop the server, press `Ctrl + C` in the server terminal.

## Key Concepts Demonstrated

### 1. Socket Programming

Java's `ServerSocket` and `Socket` classes establish TCP connections between the server and clients.

### 2. Multithreading

Each client is handled by a separate thread, allowing multiple clients to communicate with the server concurrently.

### 3. Message Broadcasting

The server maintains a collection of connected clients and sends each received message to the clients in that collection.

### 4. Runnable Interface

The `Runnable` interface defines the communication task performed by each `ClientHandler`.

### 5. Concurrent Collections

`ConcurrentHashMap.newKeySet()` provides a thread-safe collection for tracking connected clients.

## Limitations

* The server uses a fixed port number.
* Client identifiers are based on connection information rather than usernames.
* Messages are not encrypted.
* The application does not use a database to store chat messages.
* The server must remain running for clients to communicate.

## Future Improvements

* Add usernames for clients.
* Store chat history in files or a database.
* Add private messaging between clients.
* Introduce a graphical user interface.
* Implement secure communication.

## Learning Outcomes

By developing this project, I practised:

* Java TCP socket programming.
* Client-server architecture.
* Multithreaded application development.
* Concurrent collections.
* Message broadcasting.
* Resource management and disconnection handling.

## Author

**Pranav Avinash Narkhede**

Computer Engineering Student

GitHub: [pranavavinashnarkhede](https://github.com/pranavavinashnarkhede)

---

*This project was developed for learning and demonstrating Java networking and multithreading concepts.*
