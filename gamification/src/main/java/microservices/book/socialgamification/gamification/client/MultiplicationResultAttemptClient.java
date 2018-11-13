package microservices.book.socialgamification.gamification.client;

import microservices.book.socialgamification.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This interface allows to connect to Multiplication Microservice
 * agnostic communication
 */
public interface MultiplicationResultAttemptClient {

    MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationId);
}
