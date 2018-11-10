package microservices.book.gamification.repository;

import microservices.book.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

    // Get all BadgeCards for a user ordered by most recent
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
