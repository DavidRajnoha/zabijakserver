package com.example.zabijakserver.Repositories;

import com.example.zabijakserver.Entities.KillLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KillLogRepository extends JpaRepository<KillLog, Long> {
    List<KillLog> findByKillerId(Integer killerId);

    List<KillLog> findByGame_Id(Long id);
}
