package com.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class RemoteServiceApplication {
    @Autowired
    TestRepository testRepository;

    public static void main(String[] args) {
        SpringApplication.run(RemoteServiceApplication.class, args);
    }

    @RequestMapping("/")
    public List<Test> test() {
        testRepository.save(new Test());
        return testRepository.findAll();
    }
}
