package microservices.book.socialmultiplication.multiplication.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import microservices.book.socialmultiplication.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    @Getter
    public static final class ResultResponse {
        private final boolean correct;
    }
}
