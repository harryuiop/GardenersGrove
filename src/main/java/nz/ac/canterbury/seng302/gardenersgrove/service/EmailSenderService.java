package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Service about sending email in server side
 *
 */
@Service
public class EmailSenderService {

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    // Sets the email address that the email will be sent FROM
    @Value("${spring.mail.username}") private String sender;

    /**
     * constructor EmailSenderService
     *
     * @param javaMailSender JavaMailSender that will be autowired
     */
    public EmailSenderService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sending an email to a user
     *
     * @param emailTitle Email title
     * @param htmlContent Email contents in html format
     * @param sendTo recipient
     * @return true if email send successfully false otherwise
     */
    private boolean sendEmail(String emailTitle, String htmlContent, String sendTo) {
        try {
            logger.info("Start Sending an email to a recipient");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // create an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(sendTo);
            messageHelper.setSubject(emailTitle);
            messageHelper.setText(htmlContent, true);

            // send an email
            javaMailSender.send(message);
            logger.debug("Success to send the email to the recipient");
            return true;
        } catch (MessagingException e) {
            logger.debug("Failing to Send an email to the recipient");
            logger.error(e.toString());
            return false;
        }


    }

    /**
     * method to send verification email to register user account
     *
     * @param user User Entity
     * @param template html file to send an email
     */
    public boolean sendRegistrationEmail (User user, String template) {

        // generate token
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
     * Sending a test email using test.html as a body of the email.
     *
     * @param emailTitle email title
     * @param file html file that will be an email contents
     * @param link link that will be provided to a user
     * @param sendTo email address to send
     * @return boolean true if success to send the email, false otherwise
     */
    public boolean sendTestEmail(String emailTitle, File file, String link, String sendTo) {
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            String message = new String(content, StandardCharsets.UTF_8);
            String pastATag = "<a id=\"link\">";
            String replacement = String.format("<a id=\"link\" href=\"%s\">", link);
            message = message.replaceAll(pastATag, replacement);
            return this.sendEmail(emailTitle, message, sendTo);
        } catch (IOException e) {
            return false;
        }
    }

}
