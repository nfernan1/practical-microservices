package microservices.book.socialgamification.gamification.service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.socialgamification.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.socialgamification.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.socialgamification.gamification.domain.Badge;
import microservices.book.socialgamification.gamification.domain.BadgeCard;
import microservices.book.socialgamification.gamification.domain.GameStats;
import microservices.book.socialgamification.gamification.domain.ScoreCard;
import microservices.book.socialgamification.gamification.repository.BadgeCardRepository;
import microservices.book.socialgamification.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    public static final int LUCKY_NUMBER = 42;

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;
    private MultiplicationResultAttemptClient attemptClient;

    public GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository,
                           MultiplicationResultAttemptClient attemptClient) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
        this.attemptClient = attemptClient;
    }

    /**
     * New attempt from a user
     *
     * @param userId    user's unique id
     * @param attemptId attempt id for extra data
     * @param correct   if attempt was correct
     * @return {@link GameStats} object containing score and badges obtained
     */
    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
        if (correct) {
            ScoreCard scoreCard = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(scoreCard);

            log.info("User with id {} scored {} points for attempt id {}", userId, scoreCard, scoreCard.getScore(), attemptId);

            List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
            return new GameStats(userId,
                    scoreCard.getScore(),
                    badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
        }
        return GameStats.emptyStats(userId);
    }

    /**
     * Checks total score and different scores to obtain different badges
     */
    private List<BadgeCard> processForBadges(Long userId, Long attemptId) {
        List<BadgeCard> badgeCards = new ArrayList<>();
        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("New score for user {} is {}", userId, totalScore);
        List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
        List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        // Badges on score
        checkAndGiveBadgeBasedOnScore(
                badgeCardList,
                Badge.BRONZE_MULTIPLICATOR,
                totalScore, 100, userId).ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(
                badgeCardList,
                Badge.SILVER_MULTIPLICATOR,
                totalScore, 500, userId).ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(
                badgeCardList,
                Badge.GOLD_MULTIPLICATOR,
                totalScore, 999, userId).ifPresent(badgeCards::add);

        // First won badge
        if (scoreCardList.size() == 1 &&
                !containsBadge(badgeCardList, Badge.FIRST_WON)) {
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
            badgeCards.add(firstWonBadge);
        }

        // Lucky Number
        MultiplicationResultAttempt attempt = attemptClient.retrieveMultiplicationResultAttemptById(userId);
        if (!containsBadge(badgeCardList, Badge.LUCKY_NUMBER) &&
                (LUCKY_NUMBER == attempt.getMultiplicationFactorA() ||
                        LUCKY_NUMBER == attempt.getMultiplicationFactorB())) {

            BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
            badgeCards.add(luckyNumberBadge);
        }

        return badgeCards;
    }

    private BadgeCard giveBadgeToUser(Badge firstWon, Long userId) {
        BadgeCard badgeCard = new BadgeCard(userId, firstWon);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge {}", userId, firstWon);
        return badgeCard;
    }

    // Checks to see if badge is in users list already
    private boolean containsBadge(List<BadgeCard> badgeCardList, Badge firstWon) {
        return badgeCardList.stream().anyMatch(b -> b.getBadge().equals(firstWon));
    }

    // Checks current score against different thresholds to gain badges
    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(List<BadgeCard> badgeCardList,
                                                              Badge badge, int totalScore,
                                                              int scoreThreshold, Long userId) {
        if (totalScore >= scoreThreshold && !containsBadge(badgeCardList, badge)) {
            return Optional.of(giveBadgeToUser(badge, userId));
        }
        return Optional.empty();
    }

    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        return new GameStats(userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }
}
