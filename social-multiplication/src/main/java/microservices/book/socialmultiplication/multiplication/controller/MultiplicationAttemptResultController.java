package microservices.book.socialmultiplication.multiplication.controller;

import microservices.book.socialmultiplication.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public final class MultiplicationAttemptResultController {

    private final MultiplicationService multiplicationService;

    @Autowired
    public MultiplicationAttemptResultController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }
    private static final class ResultResponse {
        private final boolean correct;
        public ResultResponse(boolean correct) {
            this.correct = correct;
        }
    }

    @PostMapping
    ResponseEntity<ResultResponse> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
        return ResponseEntity.ok(
                new ResultResponse(multiplicationService
                        .checkAttempt(multiplicationResultAttempt)));
    }
}
