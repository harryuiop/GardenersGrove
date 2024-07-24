package nz.ac.canterbury.seng302.gardenersgrove.unit;

import nz.ac.canterbury.seng302.gardenersgrove.controller.validation.FormValuesValidator;
import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import nz.ac.canterbury.seng302.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.seng302.gardenersgrove.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class FormValuesValidationTest {
    FormValuesValidator formValuesValidator;
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void setUP() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
        formValuesValidator = new FormValuesValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ñ", "Text"})
    void checkContainsText_validText_returnTrue(String value){
        Assertions.assertTrue(formValuesValidator.checkContainsText(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "!", ".", "#$%^"})
    void checkContainsText_invalidText_returnFalse(String value){
        Assertions.assertFalse(formValuesValidator.checkContainsText(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwertyuiopasdfghjklzxcvbnmABC -'", "Алекс", "알렉스", "簡", "Джэйн", "เจน", "Māori"})
    void checkString_validString_returnTrue(String value) {
        Assertions.assertTrue(formValuesValidator.checkCharacters(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"!@#$%{}\"", "\\ ps %", "¬","¿","¾"})
    void checkString_invalidString_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkCharacters(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwertyuiopasdfghjklzxcvbnmABC -/'", "Алекс", "알렉스", "簡", "Джэйн", "เจน", "Māori", "/"})
    void checkCharactersWithForwardSlash_validCharacters_returnTrue(String value) {
        Assertions.assertTrue(formValuesValidator.checkCharactersWithForwardSlash(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"!@#$%{}\"", "\\ ps %", "¬","¿","¾"})
    void checkCharactersWithForwardSlash_invalidString_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkCharactersWithForwardSlash(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\n"})
    void checkName_blankName_returnTrue(String value) {
        Assertions.assertTrue(formValuesValidator.checkBlank(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Something", "!", " ¿ ", "~ "})
    void checkName_filledName_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkBlank(value));
    }

    @ParameterizedTest
    @ValueSource(floats = {1.5f, 2f, 3.0f, 0.001f})
    void checkSize_ValidSize_returnTrue(Float size) {
        Assertions.assertTrue(formValuesValidator.checkSize(size));
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.5f, -2f, -3.0f, -0.001f, 0f})
    void checkSize_InvalidSize_returnFalse(Float size) {
        Assertions.assertFalse(formValuesValidator.checkSize(size));
    }

    @Test
    void checkDesc_InvalidDesc_returnFalse() {
        String desc = "QUOBSl9rXre7kohXEjS83RTWtw69gHoyCYLj9SLb2CvVgQcdp6xgm7mgTW3WhRkabgyZ60AyhLkSo8zEGlkUW13y 390JTF8nnrPBT0GOxYzW5OrkeYqQLg8vubaMfgESUNudCR1Px0GadxfmNhGl9qyEAoRKksJO3n3HJ6ihPVjtFHbOuxbJ PTjKrNBUoBtKEAXIzKsK3jgmKP4WKTnbimPbAhfs0FbJqrzTPoBH 61smRFEcRUG6NRp61hqWtlFYxaIeSoxY7 Z68qwOvZFWAdhMdm7eedmDXvJYYJSTb1QVjAImuflA Rtblxom3sthvIX9vVs70I8pPlLcxaIFcy0POUPSCkcmLP7 U99cb0zplM5UX5cSKbHdHL 4uzDvpdHtV1vioiuIhm2olRJwTXJARB011sGyr55g0E05e4v bVCSDy0MuYDGQ770yiXyXawAEFm5jv2rIdoFBJEupmTmaErM KwmDbLr5oNyV1JreejyEIBpc6s7JHmUj1234567890";
        Assertions.assertFalse(formValuesValidator.checkDescription(desc));
    }

    @Test
    void checkDesc_ValidDesc_returnTrue() {
    String desc = "wdfghjio(*&^%$#f5678";
        Assertions.assertTrue(formValuesValidator.checkDescription(desc));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jane", "ü ", "HELLO", "hello", "Al", "Mary-Anne", "Ta'Quan", "a D"})
    void checkValidName_returnTrue(String name) {
        Assertions.assertTrue(formValuesValidator.checkUserName(name));
    }

    @ParameterizedTest
    @ValueSource(strings={"123", "a4eva", "a-", "spo'", "*", "$", "()", "-anna"})
    void checkInvalidName_returnFalse(String name) {
        Assertions.assertFalse(formValuesValidator.checkUserName((name)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jane", "123456789012345678901234567890123456789012345678901234567890234", "a", ""})
    void checkValidNameLength_returnTrue(String name) {
        Assertions.assertTrue(formValuesValidator.checkNameLength(name));
    }

    @Test
    void checkInvalidNameLength_returnFalse() {
        String name = "AaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaA";
        Assertions.assertFalse(formValuesValidator.checkNameLength(name));
    }

    @Test
    void checkInvalidPlantNameLength_returnFalse() {
        String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Assertions.assertFalse(formValuesValidator.checkPlantNameLength(name));
    }

    @Test
    void checkValidPlantNameLength_returnTrue() {
        String name ="aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Assertions.assertTrue(formValuesValidator.checkPlantNameLength(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {120, 21, 0, 119, 13, 50, 60, 1})
    void checkUnder120Valid_returnTrue() {
        int age = 120;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertTrue(formValuesValidator.checkUnder120(dob));
    }

    @ParameterizedTest
    @ValueSource(ints = {121, 1000000000, 1000, 1005000})
    void checkOver120Valid_returnFalse() {
        int age = 121;
        String dob = LocalDate.now().minusYears(age).toString();
        Assertions.assertFalse(formValuesValidator.checkUnder120(dob));
    }

    @Test
    void checkEmailNotInUse_returnTrue() {
        String email = "jane@doe.nz";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Assertions.assertTrue(formValuesValidator.emailInUse(email, userService));
    }

    @Test
    void checkEmailInUse_returnFalse() {
        String email = "jane@doe.nz";
        String firstName = "Jane";
        String lastName = "Doe";
        String password = "abc!1E";
        String dob = LocalDate.now().minusYears(20).toString();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(new User(email, firstName, lastName,password,dob));
        Assertions.assertFalse(formValuesValidator.emailInUse(email, userService));
    }
    @ParameterizedTest
    @ValueSource(strings = {"1", "100", "9923", "999999999", "01", ""})
    void checkValidPlantCount_returnTrue(String value) {
       Assertions.assertTrue(formValuesValidator.checkValidPlantCount(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0", "abc", "#!12", "9999999999", "0", "-1", "    ", "1  ", "  1", "1 2"})
    void checkValidPlantCount_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkValidPlantCount(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abcdefghijklmnopqrtsuvwxy", "âjohns-tag", "johns_tag", "johns tag", "john's tag", "john\"s-tag", "it", "éß"

    })
    void checkTagName_validTags_returnTrue(String value) {
        Assertions.assertTrue(formValuesValidator.checkTagName(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a!b", "c@d", "e#f", "g$h", "i%j", "k^l", "m&n", "o*p", "q(r", "s)t", "u+v", "w=x", "y:z", "ß", "ä-", "_Ï"
    })
    void checkTagName_invalidTags_returnFalse(String value) {
        Assertions.assertFalse(formValuesValidator.checkTagName(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ß", "ßßßßßßßßßßßßßßßßßßßßßßßßß"})
    void checkTagLength_validTags_returnTrue(String value) {
        Assertions.assertTrue(formValuesValidator.checkTagNameLength(value));
    }

    @Test
    void checkTagLength_invalidTags_returnFalse() {
        String value = "ßßßßßßßßßßßßßßßßßßßßßßßßßB";
        Assertions.assertFalse(formValuesValidator.checkTagNameLength(value));
    }
}
