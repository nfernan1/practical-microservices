package microservices.book.socialmultiplication.multiplication.repository;

import microservices.book.socialmultiplication.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {

    // Return latest 5 attempts by user
    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
    MultiplicationResultAttempt findOne(Long resultId);
}
