package com.example.zabijakserver;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KillLogRepository extends JpaRepository<KillLog, Long> {
    List<KillLog> findByKillerId(Long killerId);

    List<KillLog> findByGame_Id(Long id);
}
