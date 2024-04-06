package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailSenderService {


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
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // create an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(sendTo);
            messageHelper.setSubject(emailTitle);
            messageHelper.setText(htmlContent, true);

            // send an email
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    /**
     * method to send verification email to register user account
     * @param user User Entity
     */
    public boolean sendRegistrationEmail (User user, String template) {

        Random random = new Random();
        int digit = random.nextInt(10000, 1000000);
        String token = String.format("%06d", digit);
        user.setToken(token);   // assign the token to user
        String emailTitle = "GARDENER'S GROVE :: REGISTER YOUR EMAIL ::";

        // model for email contents
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", user.getFirstName());
        model.put("lastName", user.getLastName());
        model.put("token", user.getToken());

        // create email content
        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process(template, context);

        return this.sendEmail(emailTitle, htmlContent, user.getEmail());
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
        try {
            String htmlContent = getEmailContent(file, link);
            return this.sendEmail(emailTitle, htmlContent, sendTo);
        } catch (IOException e) {
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
