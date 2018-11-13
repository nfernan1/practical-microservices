package microservices.book.socialgamification.gamification.client;

import microservices.book.socialgamification.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationhost;

    @Autowired
    public MultiplicationResultAttemptClientImpl(RestTemplate restTemplate, String multiplicationhost) {
        this.restTemplate = restTemplate;
        this.multiplicationhost = multiplicationhost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(Long multiplicationId) {
        return restTemplate.getForObject(
                multiplicationhost + "/results/" + multiplicationId, MultiplicationResultAttempt.class);
    }
}
