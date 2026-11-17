# rabbitmq-testclient
This is a simple, Java-based command line client for RabbitMq.

## Usage

To run a RabbitMq container with podman:

```bash
podman run -d --name rabbitmq -p 5672:5672 -e RABBITMQ_DEFAULT_USER=testuser -e RABBITMQ_DEFAULT_PASS=testpwd rabbitmq:latest
```

The application takes a file path as a command line argument, connection information will be read from there. If not provided, it will try to read `config.properties` from the folder where the application is running.

Sample properties file: [config.properties](config.properties)

