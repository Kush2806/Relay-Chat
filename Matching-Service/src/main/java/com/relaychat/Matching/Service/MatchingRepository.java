package com.relaychat.Matching.Service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends MongoRepository<UserMatch, String> {
    List<UserMatch> findByActive(boolean active);
}
