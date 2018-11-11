package microservices.book.socialgamification.gamification.service;

import microservices.book.socialgamification.gamification.domain.LeaderBoardRow;
import microservices.book.socialgamification.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private ScoreCardRepository scoreCardRepository;

    public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    /**
     * Retrieves current leader board with top score users
     *
     * @return the users with highest score
     */
    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        return scoreCardRepository.findFirst10();
    }
}
