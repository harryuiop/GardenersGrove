package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * Email Controller - Controller for sending email in server side
 */
@RestController
public class EmailController {

    Logger logger = LoggerFactory.getLogger(GardenFormController.class);
    /**
     * Email Service
     */
    final EmailSenderService emailService;

    /**
     * Constructor of EmailController
     * @param emailService @link{EmailSenderService} that brings html email contents and send to receiver
     */
    public EmailController (EmailSenderService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/register/register-email")
    public String sendRegistrationEmail (@RequestParam("email") String email) {
        // TODO - Implement this methods
        return null;
    }

    /**
     * Testing controller and service here
     * use url: http://localhost:8080/send-test-email?emailAddress={email address to send} to test
     * @return String "Success" if success "Fail" otherwise
     */
    @RequestMapping("/send-test-email")
    public String sendTestEmail(@RequestParam("emailAddress") String testEmailAddress) {

        File testHtml = new File("src/main/resources/static/email/test.html");

        // Link to provide to users in email
        String testLink = "http://localhost:8080/";

        // an email address to send test email.
        String testEmailTo = testEmailAddress;
        String title = "This is Test email";

        boolean result = emailService.sendTestEmail(title, testHtml, testLink, testEmailTo);
        logger.info("Email sending test is " + (result ? "Success" : "Fail"));
        return result ? "Success" : "Fail";
    }
}
