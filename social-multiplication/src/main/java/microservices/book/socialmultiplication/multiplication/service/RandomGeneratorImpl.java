package microservices.book.socialmultiplication.multiplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeneratorImpl implements RandomGeneratorService {

    final static int MIN = 11;
    final static int MAX = 99;

    @Override
    public int generateRandomFactor() {

        return new Random().nextInt((MAX - MIN) + 1) + MIN;
    }
}
