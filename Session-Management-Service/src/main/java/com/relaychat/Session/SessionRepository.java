package com.relaychat.Session;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {
    List<UserSession> findByActive(boolean active);
    UserSession findByUserId(String userId);
}