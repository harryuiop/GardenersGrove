package nz.ac.canterbury.seng302.gardenersgrove.service.impl;

import jakarta.mail.internet.MimeMessage;
import nz.ac.canterbury.seng302.gardenersgrove.controller.HomeController;
import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;


    public boolean sendEMail(String email, File file, String link) {
        logger.info("Preparing to send an verification Email");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            // get email contents
            String htmlContent = getEmailContent(file, link);

            // prepare to send an email
            messageHelper.setFrom(sender);
            messageHelper.setTo(email);
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

    private String getEmailContent (File file, String link) throws IOException {
        logger.info("Generating email contents");
        // TODO - Bring html contents to here
        byte[] content = Files.readAllBytes(file.toPath());
        String message = new String(content, StandardCharsets.UTF_8);
//        String link = "http://localhost:8080/email-registratoin";
        // This is temporary value
        String pastATag = "<a id=\"verify-link\">";
        String replacement = String.format("<a id=\"verify-link\" href=\"%s\">", link);
        message = message.replaceAll(pastATag, replacement);

        return message;
    }
}
