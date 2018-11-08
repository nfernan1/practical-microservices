package microservices.book.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// Badge associated with a user and time they received it
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class BadgeCard {

    @Id
    @GeneratedValue
    @Column(name="BADGE_ID")
    private final Long BadgeId;

    private final Long userId;
    private final Long badgeTimestamp;
    private final Badge badge;

    public BadgeCard() {
        this(null, null, (long) 0, null);
    }

    public BadgeCard(final Long userId, final Badge badge) {
        this(null, userId, System.currentTimeMillis(), badge);
    }
}
