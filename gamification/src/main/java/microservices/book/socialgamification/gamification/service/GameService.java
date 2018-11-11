package microservices.book.socialgamification.gamification.service;

import microservices.book.socialgamification.gamification.domain.GameStats;

public interface GameService {

    /**
     * New attempt from a user
     * @param userId user's unique id
     * @param attemptId attempt id for extra data
     * @param correct if attempt was correct
     * @return {@link GameStats} object containing score and badges obtained
     */
    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

    // Gets stats for a user
    GameStats retrieveStatsForUser(Long userId);
}
