package nz.ac.canterbury.seng302.gardenersgrove.integration.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import static nz.ac.canterbury.seng302.gardenersgrove.config.UriConfig.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SearchResultsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchUser_noWhitespace_correctRedirect() throws Exception {
        String search = "user";
        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_RESULTS_STRING)
                        .param("searchUser", search))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(getExpectedRedirectedUrl("user")));
    }

    @Test
    void searchUser_whitespace_correctRedirect() throws Exception {
        String search = "  user ";
        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_RESULTS_STRING)
                        .param("searchUser", search))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(getExpectedRedirectedUrl("user")));
    }

    @Test
    void searchUser_whitespaceInName_correctRedirect() throws Exception {
        String search = "  use r ";
        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_RESULTS_STRING)
                        .param("searchUser", search))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern(getExpectedRedirectedUrl("use+r")));
    }

    private String getExpectedRedirectedUrl(String searchUser) {
        return UriComponentsBuilder.fromPath(SEARCH_RESULTS_STRING)
                .queryParam("searchUser", searchUser)
                .build().toUriString();
    }

}
