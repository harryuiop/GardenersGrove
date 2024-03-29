package nz.ac.canterbury.seng302.gardenersgrove;

import nz.ac.canterbury.seng302.gardenersgrove.service.EmailSenderService;
import nz.ac.canterbury.seng302.gardenersgrove.service.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SendingEmailTest {
    @Autowired JavaMailSender javaMailSender;
    private EmailSenderService emailSenderService = new EmailSenderServiceImpl();
    private final File TEST_HTML = new File("static/email/test.html");
    private final String LINK = "localhost:8080/login";
    private final String TEST_EMAIL = "hya62@uclive.ac.nz";


    @Test
    void sendTestEmail() {
        boolean result = emailSenderService.sendEMail(TEST_EMAIL, TEST_HTML, LINK);
        Assertions.assertTrue(result);
    }


}
