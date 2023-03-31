package com.graccasoft.paxful.service;

import com.graccasoft.paxful.dto.OptionRequest;
import com.graccasoft.paxful.model.BotOption;

import java.util.List;

public interface BotOptionService {
    List<BotOption> getAllOptions();
    List<BotOption> saveOption(List<OptionRequest> optionRequest);
    BotOption getOption(String name);
}
