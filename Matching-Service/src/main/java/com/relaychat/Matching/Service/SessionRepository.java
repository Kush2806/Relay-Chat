package com.relaychat.Matching.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> { 
    List<UserSession> findByActive(boolean active);
    List<UserSession> findByActiveAndMatched(boolean active,boolean matched);
    UserSession findByUserId(String userId);
}
