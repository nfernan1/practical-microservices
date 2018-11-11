package microservices.book.socialgamification.gamification.service;

import microservices.book.socialgamification.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {

    /**
     * Retrieves current leader board with top score users
     * @return the users with highest score
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
