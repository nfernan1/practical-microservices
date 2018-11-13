package microservices.book.socialmultiplication.multiplication.repository;

import microservices.book.socialmultiplication.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

}
