package ru.mentee.power.crm.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HelloCrmServer {
    private final HttpServer server;
    private final int port;

    public HelloCrmServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void start() {
        server.createContext("/hello", new HelloHandler());
        server.start();
        System.out.println("Server started on http://localhost:" + port);
    }

    public void stop() {
        server.stop(0);
    }

    static class HelloHandler implements HttpHandler {
        String html = """
                <!DOCTYPE html>
                <html>
                    <head><title>CRM</title></head>
                    <body><h1>Hello CRM!</h1></body>
                </html>
                """;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Received request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(html.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        }
    }
}
