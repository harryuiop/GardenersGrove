package nz.ac.canterbury.seng302.gardenersgrove.service;

import java.io.File;

public interface EmailSenderService {
    boolean sendEMail(String email, File file, String link);
    }
