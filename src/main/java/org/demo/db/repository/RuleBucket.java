package org.demo.db.repository;

import org.demo.db.entity.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by barry.wong
 */
public interface RuleBucket extends MongoRepository<Rule, String> {

    public Rule findByName(String name);

}