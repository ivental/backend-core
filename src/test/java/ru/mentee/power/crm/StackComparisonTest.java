package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mentee.power.crm.spring.Application;

import java.net.http.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.*;

class StackComparisonTest {
    private static Tomcat tomcat;
    private static ConfigurableApplicationContext springContext;
    private static int servletPort;
    private static int springPort;
    private static long servletStartupMs;
    private static long springStartupMs;
    private HttpClient httpClient;

    @BeforeAll
    static void startServers() throws Exception {
        tomcat = new Tomcat();
        tomcat.setPort(0);
        Path tempDir = Files.createTempDirectory("tomcat");
        Context ctx = tomcat.addContext("", tempDir.toString());

        Tomcat.addServlet(ctx, "testServlet", new jakarta.servlet.http.HttpServlet() {
            @Override
            protected void doGet(jakarta.servlet.http.HttpServletRequest req,
                                 jakarta.servlet.http.HttpServletResponse resp) {
                try {
                    resp.setContentType("text/html");
                    resp.getWriter().write(
                            "<html><body><table>" +
                                    "<tr><td>Lead 1</td></tr>" +
                                    "<tr><td>Lead 2</td></tr>" +
                                    "<tr><td>Lead 3</td></tr>" +
                                    "<tr><td>Lead 4</td></tr>" +
                                    "<tr><td>Lead 5</td></tr>" +
                                    "</table></body></html>"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ctx.addServletMappingDecoded("/leads", "testServlet");
        long start = System.nanoTime();
        tomcat.start();
        servletStartupMs = (System.nanoTime() - start) / 1_000_000;
        servletPort = tomcat.getConnector().getLocalPort();
        String[] args = {"--server.port=0"};
        start = System.nanoTime();
        springContext = SpringApplication.run(Application.class, args);
        springStartupMs = (System.nanoTime() - start) / 1_000_000;
        springPort = Integer.parseInt(
                springContext.getEnvironment().getProperty("local.server.port", "8081")
        );
    }

    @AfterAll
    static void stopServers() throws Exception {
        if (springContext != null) {
            SpringApplication.exit(springContext);
        }
        if (tomcat != null) {
            tomcat.stop();
            tomcat.destroy();
        }
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    private int countTableRows(String html) {
        if (html == null || html.isEmpty()) {
            return 0;
        }
        Pattern pattern = Pattern.compile("<tr", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    @Test
    @DisplayName("Оба стека должны возвращать лидов в HTML таблице")
    void shouldReturnLeadsFromBothStacks() throws Exception {
        HttpRequest servletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + servletPort + "/leads"))
                .GET()
                .build();
        HttpRequest springRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + springPort + "/leads"))
                .GET()
                .build();
        HttpResponse<String> servletResponse = httpClient.send(
                servletRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> springResponse = httpClient.send(
                springRequest, HttpResponse.BodyHandlers.ofString());
        assertThat(servletResponse.statusCode()).isEqualTo(200);
        assertThat(springResponse.statusCode()).isEqualTo(200);
        assertThat(servletResponse.body()).contains("<table");
        assertThat(springResponse.body()).contains("<table");
        int servletRows = countTableRows(servletResponse.body());
        int springRows = countTableRows(springResponse.body());
        assertThat(servletRows)
                .as("Количество лидов должно совпадать")
                .isEqualTo(springRows);
        System.out.printf("Servlet: %d лидов, Spring: %d лидов%n",
                servletRows, springRows);
    }

    @Test
    @DisplayName("Измерение времени старта обоих стеков")
    void shouldMeasureStartupTime() {
        System.out.println("=== Сравнение времени старта ===");
        System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
        System.out.printf("Spring Boot: %d ms%n", springStartupMs);
        System.out.printf("Разница: Spring %s на %d ms%n",
                springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
                Math.abs(springStartupMs - servletStartupMs));
        assertThat(servletStartupMs).isLessThan(10_000);
        assertThat(springStartupMs).isLessThan(15_000);
    }
}