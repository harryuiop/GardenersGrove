package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Service about sending email in server side
 *
 */
@Service
public class EmailSenderService {

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    private UserService userService;


    // Sets the email address that the email will be sent FROM
    @Value("${spring.mail.username}") private String sender;

    /**
     * constructor EmailSenderService
     *
     * @param javaMailSender JavaMailSender that will be autowired
     */
    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender, TemplateEngine templateEngine, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    /**
     * method to email the user
     *
     * @param user User Entity
     * @param template html file to send an email
     * @param emailType the type of email to send, e.g. registration email
     */
    public boolean sendEmail(User user, String template, String emailType) {
        String emailTitle;

        switch (emailType) {
            case "registration":
                user = userService.grantUserToken(user);
                emailTitle = "GARDENER'S GROVE :: REGISTER YOUR EMAIL ::";
                break;
            case "passwordUpdated":
                emailTitle = "GARDENER'S GROVE :: YOUR PASSWORD HAS BEEN UPDATED ::";
                break;
            default:
                emailTitle = "GARDENER'S GROVE";
        }

        // model for email contents
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", user.getFirstName());
        model.put("lastName", user.getLastName());
        model.put("token", user.getToken());

        // create email content
        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process(template, context);

        try {
            logger.info("Start Sending an email to a recipient");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // create an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(user.getEmail());
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

}
