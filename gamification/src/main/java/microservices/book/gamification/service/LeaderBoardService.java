package microservices.book.gamification.service;

import microservices.book.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {

    /**
     * Retrieves current leader board with top score users
     * @return the users with highest score
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
