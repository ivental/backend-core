package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        InMemoryLeadRepository repository = new InMemoryLeadRepository();
        LeadService leadService = new LeadService(repository);

        leadService.addLead("ivental@gmail.com", "+7911", "Megacorp", LeadStatus.NEW);
        leadService.addLead("RE@umbrella.com", "+7912", "Umbrella Inc", LeadStatus.CONTACTED);
        leadService.addLead("arasaka@cp77.com", "+7913", "Arasaka Corp", LeadStatus.QUALIFIED);
        leadService.addLead("militech@mt.com", "+7914","Militech", LeadStatus.NEW);
        leadService.addLead("java@java.com", "+7915","Java Corporation", LeadStatus.NEW);


        System.out.println("Добавлены " + leadService.findAll().size() + " тестовых лидов");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();
        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        context.getServletContext().setAttribute("leadService", leadService);
        tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
        context.addServletMappingDecoded("/leads", "LeadListServlet");
        tomcat.start();
        System.out.println("Tomcat started on http://localhost:8080");
        System.out.println("Open http://localhost:8080/leads in your browser");
        tomcat.getServer().await();

    }
}
