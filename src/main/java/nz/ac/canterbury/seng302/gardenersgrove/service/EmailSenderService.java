package nz.ac.canterbury.seng302.gardenersgrove.service;

import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.controller.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class EmailSenderService {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    private JavaMailSender javaMailSender;

    // email address that send FROM
    @Value("${spring.mail.username}") private String sender;

    /**
     * constructor EmailSenderService
     * @param javaMailSender JavaMailSender that will be autowired
     */
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sending an email to a user
     * @param emailTo email address to send
     * @param file html file that will be an email contents
     * @param link link that will be provided to a user
     * @return boolean true if success to send the email, false otherwise
     */
    public boolean sendEmail(String emailTo, File file, String link) {
        logger.info("Preparing to send an verification Email");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            // get email contents
            String htmlContent = getEmailContent(file, link);

            // prepare to send an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(emailTo);
            final String VERIFY_EMAIL_SUBJECT = "GARDENERS GROVE :: VERIFY YOUR EMAIL!!!";
            messageHelper.setSubject(VERIFY_EMAIL_SUBJECT);
            messageHelper.setText(htmlContent, true);

            //send an email
            javaMailSender.send(message);

        } catch (Exception exception) {
            logger.error(exception.toString());
            return false;
        }
        return true;
    }

    /**
     * extract a html source as string and return it
     * @param file html file that will be an email contents
     * @param link link that will be provided to a user
     * @return a string with html format to send as email contents
     * @throws IOException
     */
    private String getEmailContent (File file, String link) throws IOException {
        logger.info("Generating email contents");
        byte[] content = Files.readAllBytes(file.toPath());
        String message = new String(content, StandardCharsets.UTF_8);
        String pastATag = "<a id=\"verify-link\">";
        String replacement = String.format("<a id=\"verify-link\" href=\"%s\">", link);
        message = message.replaceAll(pastATag, replacement);

        return message;
    }
}
