package microservices.book.socialgamification.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/*
Score linked to game and user
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class ScoreCard {

    public static final int DEFAULT_SCORE = 10;

    @Id
    @GeneratedValue
    @Column(name="CARD_ID")
    private final Long cardId;

    @Column(name="USER_ID")
    private final Long userId;

    @Column(name="ATTEMPT_ID")
    private final Long attemptId;

    @Column(name="SCORE_TS")
    private final Long scoreTimestamp;

    @Column(name = "SCORE")
    private final int score;

    // Need empty constructor for JSON
    public ScoreCard() {
        this(null, null, null, (long) 0, 0);
    }

    public ScoreCard(final Long userId, final Long attemptId) {
        this(null, userId, attemptId, System.currentTimeMillis(), DEFAULT_SCORE);
    }
}
