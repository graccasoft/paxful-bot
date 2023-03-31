package com.graccasoft.paxful.repository;

import com.graccasoft.paxful.model.BotOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotOptionRepository extends JpaRepository<BotOption, Integer> {
    Optional<BotOption> findByName(String name);
}
