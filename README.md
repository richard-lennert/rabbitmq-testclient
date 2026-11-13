# rabbitmq-testclient
This is a simple, Java-based command line client for RabbitMq.

Run a RabbitMq container: podman run -d --name rabbitmq -p 5672:5672 -e RABBITMQ_DEFAULT_USER=testuser -e RABBITMQ_DEFAULT_PASS=testpwd rabbitmq:latest