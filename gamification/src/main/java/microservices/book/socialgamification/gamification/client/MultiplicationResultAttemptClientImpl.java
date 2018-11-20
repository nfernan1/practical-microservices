package microservices.book.socialgamification.gamification.client;

import microservices.book.socialgamification.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationhost;

    @Autowired
    public MultiplicationResultAttemptClientImpl(RestTemplate restTemplate,
                                                 @Value("${multiplicationHost}") final String multiplicationhost) {
        this.restTemplate = restTemplate;
        this.multiplicationhost = multiplicationhost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationId) {
        return restTemplate.getForObject(
                multiplicationhost + "/results/" + multiplicationId, MultiplicationResultAttempt.class);
    }
}
