package microservices.book.socialmultiplication.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class User {

    private final String alias;

    public User(String alias) {
        this.alias = alias;
    }
}
