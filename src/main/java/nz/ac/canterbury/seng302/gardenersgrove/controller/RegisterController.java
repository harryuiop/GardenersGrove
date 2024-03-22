package nz.ac.canterbury.seng302.gardenersgrove.controller;

import nz.ac.canterbury.seng302.gardenersgrove.entity.Users;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

  Logger logger = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  UserService userService;

  @GetMapping("/register")
  public String showRegisterPage() {
    logger.info("GET /register");
    return "register";
  }

  @PostMapping("/register")
//  public String addNewUser(
//    @RequestParam(name = "email") String email,
//    @RequestParam(name = "firstName") String firstName,
//    @RequestParam(name = "lastName") String lastName,
//    @RequestParam(name = "address") String address,
//    @RequestParam(name = "password") String password,
//    @RequestParam(name = "dateOfBirth") String dateOfBirth
//  ) {
//    logger.info("This is a debug message");
//    logger.info("POST /register");
//    userService.addUsers(
//      new Users(email, firstName, lastName, address, password, dateOfBirth)
//    );
    public String showHomePage() {
      return "homeTemplate";
    }
}
