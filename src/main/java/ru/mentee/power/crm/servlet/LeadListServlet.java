package ru.mentee.power.crm.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.service.LeadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/leads")
public class LeadListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("GET /leads request received");
        LeadService leadService = (LeadService) getServletContext().getAttribute("leadService");
        List<Lead> leads = leadService.findAll();
        System.out.println("Found " + leads.size() + " leads");
        System.out.println("Response sent successfully");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("    <title>CRM - Лиды</title>");
        writer.println("    <meta charset='UTF-8'>");
        writer.println("    <style>");
        writer.println("        table { border-collapse: collapse; width: 100%; }");
        writer.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        writer.println("        th { background-color: #f2f2f2; }");
        writer.println("        tr:nth-child(even) { background-color: #f9f9f9; }");
        writer.println("    </style>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("    <h1>Список лидов</h1>");

        if (leads.isEmpty()) {
            writer.println("    <p>Нет лидов</p>");
        } else {
            writer.println("    <table>");
            writer.println("        <tr>");
            writer.println("            <th>ID</th>");
            writer.println("            <th>Email</th>");
            writer.println("            <th>Phone</th>");
            writer.println("            <th>Company</th>");
            writer.println("            <th>Status</th>");
            writer.println("        </tr>");

            for (Lead lead : leads) {
                writer.println("        <tr>");
                writer.println("            <td>" + lead.id() + "</td>");
                writer.println("            <td>" + lead.email() + "</td>");
                writer.println("            <td>" + lead.phone() + "</td>");
                writer.println("            <td>" + lead.company() + "</td>");
                writer.println("            <td>" + lead.status() + "</td>");
                writer.println("        </tr>");
            }

            writer.println("    </table>");
        }

        writer.println("</body>");
        writer.println("</html>");

        writer.close();
    }
}


