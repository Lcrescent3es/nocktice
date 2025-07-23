package com.nocktice.nocktice.home.service;

import com.nocktice.nocktice.home.entity.Test;
import com.nocktice.nocktice.home.repository.TestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Transactional
    public Test getDataHealth() {
        return testRepository.findById(1).orElse(null);
    }
}
