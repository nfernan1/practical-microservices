package microservices.book.gamification.service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;

    public GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
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

        if (scoreCardList.size() == 1 &&
                !containsBadge(badgeCardList, Badge.FIRST_WON)) {
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
            badgeCards.add(firstWonBadge);
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
