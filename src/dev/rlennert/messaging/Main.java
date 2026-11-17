/*
 * Copyright (c) 2026 Richard Lennert
 * SPDX-License-Identifier: MIT
 */

package dev.rlennert.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

	static ConnectionProperties cp = null;

	static Connection connection = null;

	public static void main(String[] args) {

		cp = (args.length > 0) ? new ConnectionProperties(args[0]) : new ConnectionProperties("config.properties");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String input;
			while (true) {
				System.out.print("> ");
				input = reader.readLine().trim();
				switch (input) {
				case "exit":
					exit();
					break;
				case "dump":
					dump();
					break;
				case "connect":
					connect();
					break;
				case "disconnect":
					disconnect();
					break;
				case "read":
					read();
					break;
				case "count":
					count();
					break;
				case "send":
					System.out.print(">> ");
					String message = reader.readLine();
					send(message);
					break;
				case "help":
					help();
					break;
				case "status":
					status();
					break;
				case "":
					break;
				default:
					System.out.println("Unknown command: " + input);
					help();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void exit() {
		disconnect();
		System.out.println("Exiting...");
		System.exit(0);
	}

	private static void connect() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(cp.getHost());
		factory.setPort(cp.getPort());
		factory.setUsername(cp.getUsername());
		factory.setPassword(cp.getPassword());
		try {
			connection = factory.newConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Connected.");
	}

	private static void disconnect() {
		if (connection != null && connection.isOpen()) {
			try {
				connection.close();
				System.out.println("Disconnected.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Connection was already closed.");
		}
	}

	private static void send(String message) {
		System.out.println("Sending message: '" + message + "'");
		Channel channel = null;
		try {
			channel = connection.createChannel();
			channel.queueDeclare(cp.getQueue(), true, false, false, null);
			channel.basicPublish("", cp.getQueue(), null, message.getBytes());
			System.out.println("Message sent.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void read() {
		Channel channel = null;
		try {
			channel = connection.createChannel();
			if (0 < channel.messageCount(cp.getQueue())) {
				String message = new String(channel.basicGet(cp.getQueue(), true).getBody());
				System.out.println("Received message: '" + message + "'");
			} else {
				System.out.println("The queue is empty.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void count() {
		Channel channel = null;
		try {
			channel = connection.createChannel();
			System.out.println("Number of messages in queue: '" + channel.messageCount(cp.getQueue()) + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void dump() {
		System.out.println("Dumping connection properties...");
		System.out.println("Host: " + cp.getHost());
		System.out.println("Port: " + cp.getPort());
		System.out.println("Username: " + cp.getUsername());
		System.out.println("Password: " + cp.getPassword());
		System.out.println("Queue: " + cp.getQueue());
		System.out.println("Finished dumping connection properties.");
	}

	private static void help() {
		System.out.println("The following commands are supported:");
		System.out.println("	- status: prints connection status");
		System.out.println("	- connect: establishes a connection to rabbitmq");
		System.out.println("	- disconnect: disconnects from rabbitmq");
		System.out.println("	- dump: dumps all properties read from the property file");
		System.out.println("	- send: starts a new prompt (>> ), type in the message contents, when hitting enter the message is sent");
		System.out.println("	- read: reads one message from the queue");
		System.out.println("	- count: displays the number of messages currently in the queue");
		System.out.println("	- exit: disconnects from rabbitmq and closes the program");
		System.out.println("	- help: prints this help menu");
	}

	private static void status() {
		if (connection != null && connection.isOpen()) {
			System.out.println("Connected.");
		} else {
			System.out.println("Not connected.");
		}
	}

}