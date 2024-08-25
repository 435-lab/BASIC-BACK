package com.example.basicback.repository;

import com.example.basicback.model.DisasterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DisasterMessageRepository extends JpaRepository<DisasterMessage, Long> {
    List<DisasterMessage> findByLocationName(String locationName);
    boolean existsByMd101Sn(String md101Sn);
}