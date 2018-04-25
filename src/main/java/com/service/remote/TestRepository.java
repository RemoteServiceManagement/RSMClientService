package com.service.remote;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dawid on 25.04.2018 at 21:49.
 */
@Repository
public interface TestRepository extends CrudRepository<Test, Long> {
    List<Test> findAll();
}
