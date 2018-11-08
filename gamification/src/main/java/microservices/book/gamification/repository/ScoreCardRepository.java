package microservices.book.gamification.repository;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    /**
     *  Gets total score for a user, sum of scores from scorecard
     * @param userId
     * @return total score for a user
     */
    @Query("SELECT SUM(s.score) FROM microservices.book.gamification.domain.ScoreCard s " +
            "WHERE s.userId = userId GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    /**
     * Creates and returns a users LeaderBoardRow position
     * @return Leader board, sorted by highest score
     */
    @Query("SELECT NEW microservices.book.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score))" +
            "FROM microservices.book.gamification.domain.ScoreCard s GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

    /**
     * Retrieves all ScoreCards for a given user by userId
     * @param userId
     * @return
     */
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);
}
