package com.graccasoft.paxful.service;

import com.graccasoft.paxful.dto.OptionRequest;
import com.graccasoft.paxful.model.BotOption;
import com.graccasoft.paxful.repository.BotOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotOptionServiceImpl implements BotOptionService {

    private final BotOptionRepository botOptionRepository;

    public BotOptionServiceImpl(BotOptionRepository botOptionRepository) {
        this.botOptionRepository = botOptionRepository;
    }

    @Override
    public List<BotOption> getAllOptions() {
        return botOptionRepository.findAll();
    }

    @Override
    public BotOption saveOption(OptionRequest optionRequest) {
        BotOption optionToSave = botOptionRepository.findByName(optionRequest.name())
                .orElse( new BotOption(optionRequest.name(), optionRequest.value()) );

        optionToSave.setValue(optionRequest.value());
        return botOptionRepository.save(optionToSave);
    }

    @Override
    public BotOption getOption(String name) {
        return botOptionRepository.findByName(name).orElseThrow(()-> new EntityNotFoundException("Option not found"));
    }
}
