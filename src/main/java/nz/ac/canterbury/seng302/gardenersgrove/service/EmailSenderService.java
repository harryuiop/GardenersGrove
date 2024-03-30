package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.controller.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailSenderService {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    // email address that send FROM
    @Value("${spring.mail.username}") private String sender;

    /**
     * constructor EmailSenderService
     * @param javaMailSender JavaMailSender that will be autowired
     */
    public EmailSenderService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sending an email to a user
     * @param emailTitle Email title
     * @param htmlContent Email contents in html format
     * @param sendTo recipient
     * @return true if email send successfully false otherwise
     */
    private boolean sendEmail(String emailTitle, String htmlContent, String sendTo) {
        logger.info("Server is being ready to send an email.");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            // create an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(sendTo);
            messageHelper.setSubject(emailTitle);
            messageHelper.setText(htmlContent, true);

            // send an email
            javaMailSender.send(message);
            logger.info("Email created and send successfully.");
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }


    }

    public boolean sendEmailRegistration(String emailTitle, String template, Map<String, Object> model, String sendTo) {

        // create email content
        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process(template, context);

        return this.sendEmail(emailTitle, htmlContent, sendTo);
    }


    /**
     * Sending a test email
     * @param emailTitle email title
     * @param file html file that will be an email contents
     * @param link link that will be provided to a user
     * @param sendTo email address to send
     * @return boolean true if success to send the email, false otherwise
     */
    public boolean sendTestEmail(String emailTitle, File file, String link, String sendTo) {
        logger.info("Preparing to send an verification Email");

        try {
            String htmlContent = getEmailContent(file, link);
            return this.sendEmail(emailTitle, htmlContent, sendTo);
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }
    }

    /**
     * extract a html source as string and return it.
     * This is one way if we do not want to use thymeleaf.
     * @param file html file that will be an email contents
     * @param link link that will be provided to a user
     * @return a string with html format to send as email contents
     */
    private String getEmailContent (File file, String link) throws IOException {

        byte[] content = Files.readAllBytes(file.toPath());
        String message = new String(content, StandardCharsets.UTF_8);
        String pastATag = "<a id=\"link\">";
        String replacement = String.format("<a id=\"link\" href=\"%s\">", link);
        message = message.replaceAll(pastATag, replacement);
        return message;

    }
}
