/*
 * Copyright (c) 2026 Richard Lennert
 * SPDX-License-Identifier: MIT
 */

package dev.rlennert.messaging;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

class ConnectionProperties {

	private Properties properties = null;

	ConnectionProperties(String path) {
		System.out.println("Reading properties from " + path);

		try (InputStream input = new FileInputStream(path)) {

			properties = new Properties();
			properties.load(input);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	String getHost() {
		return properties.getProperty("rabbitmq.host");
	}

	int getPort() {
		return Integer.parseInt(properties.getProperty("rabbitmq.port"));
	}

	String getUsername() {
		return properties.getProperty("rabbitmq.username");
	}

	String getPassword() {
		return properties.getProperty("rabbitmq.password");
	}

	String getQueue() {
		return properties.getProperty("rabbitmq.queue");
	}

}
