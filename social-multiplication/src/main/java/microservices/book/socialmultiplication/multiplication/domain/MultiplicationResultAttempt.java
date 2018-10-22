package microservices.book.socialmultiplication.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class MultiplicationResultAttempt {

    // Attempts will have two foreign keys that link to the user and multiplication tables
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="USER_ID")
    private final User user;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="MULTIPLICATION_ID")
    private final Multiplication multiplication;

    private final int resultAttempt;
    private final boolean correct;

    public MultiplicationResultAttempt() {
        user = null;
        multiplication = null;
        resultAttempt = -1;
        correct = false;
    }

    public int getResultAttempt() {
        return resultAttempt;
    }

    public Multiplication getMultiplication() {
        return multiplication;
    }
}
