package microservices.book.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.socialmultiplication.multiplication.controller.MultiplicationAttemptResultController;
import microservices.book.socialmultiplication.multiplication.domain.Multiplication;
import microservices.book.socialmultiplication.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.multiplication.domain.User;
import microservices.book.socialmultiplication.multiplication.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationAttemptResultController.class)
public class MultiplicationAttemptResultControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParamaterizedTest(true);
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericParamaterizedTest(false);
    }

    private void genericParamaterizedTest(final boolean correct) throws Exception {
        // Given (not testing service itself)
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
                .willReturn(correct);

        User user = new User("John");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(
                user, multiplication, 3500, correct);

        // when
        MockHttpServletResponse response = mvc.perform(post("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonResultAttempt.write(multiplicationResultAttempt).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttempt.write(
                new MultiplicationResultAttempt(
                        multiplicationResultAttempt.getUser(),
                        multiplicationResultAttempt.getMultiplication(),
                        multiplicationResultAttempt.getResultAttempt(),
                        correct)).getJson());
    }

    @Test
    public void getUserStats() throws Exception {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,70);
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3500, true);
        List<MultiplicationResultAttempt> recentAttempts = Lists.newArrayList(attempt, attempt);
        given(multiplicationService.getStatsForUser("john_doe")).willReturn(recentAttempts);

        //when
        MockHttpServletResponse response = mvc.perform(
                get("/results").param("alias", "john_doe")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(recentAttempts).getJson());
    }
}
