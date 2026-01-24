package ru.mentee.power.crm.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LeadListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private LeadService leadService;

    private LeadListServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new LeadListServlet();
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        servlet.init(servletConfig);
        when(servlet.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void doGet_shouldReturnHtmlTableWithLeads() throws Exception {
        Lead lead1 = new Lead(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                "test1@example.com",
                "+79110000001",
                "Company One",
                LeadStatus.NEW
        );

        Lead lead2 = new Lead(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                "test2@example.com",
                "+79110000002",
                "Company Two",
                LeadStatus.CONTACTED
        );

        when(leadService.findAll()).thenReturn(List.of(lead1, lead2));
        servlet.doGet(request, response);
        printWriter.flush();
        String htmlOutput = stringWriter.toString();
        assertThat(htmlOutput)
                .contains("<!DOCTYPE html>")
                .contains("CRM - Лиды")
                .contains("CRM System")
                .contains("table");
        assertThat(htmlOutput)
                .contains("Email")
                .contains("Company");
        assertThat(htmlOutput)
                .contains("test1@example.com")
                .contains("Company One")
                .contains("test2@example.com")
                .contains("Company Two");
        verify(response).setContentType("text/html; charset=UTF-8");
        verify(leadService).findAll();
    }

    @Test
    void doGet_shouldSetCorrectContentType() throws Exception {
        when(leadService.findAll()).thenReturn(List.of());
        servlet.doGet(request, response);
        verify(response).setContentType("text/html; charset=UTF-8");
    }

    @Test
    void doGet_shouldHandleLeadServiceNotFound() throws Exception {
        when(servletContext.getAttribute("leadService")).thenReturn(null);
        org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> servlet.doGet(request, response)
        );
    }

    @Test
    void doGet_shouldCloseWriter() throws Exception {
        when(leadService.findAll()).thenReturn(List.of());
        servlet.doGet(request, response);
        verify(response).getWriter();
    }
}
