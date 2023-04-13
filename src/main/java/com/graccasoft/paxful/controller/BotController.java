package com.graccasoft.paxful.controller;

import com.graccasoft.paxful.dto.GenericResponse;
import com.graccasoft.paxful.dto.OptionRequest;
import com.graccasoft.paxful.model.BotOption;
import com.graccasoft.paxful.service.BotOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BotController {

    private final BotOptionService botOptionService;

    public BotController(BotOptionService botOptionService) {
        this.botOptionService = botOptionService;
    }

    @GetMapping("ping")
    public GenericResponse ping(){
        return new GenericResponse(true,"Version: 1.1");
    }

    @PostMapping("options")
    public GenericResponse updateOption(@RequestBody List<OptionRequest> optionRequest){
        botOptionService.saveOption(optionRequest);
        return new GenericResponse(true, "Option successfully updates");
    }

    @GetMapping("options")
    public List<BotOption> getOptions(){
        return botOptionService.getAllOptions();
    }

    @GetMapping("options/{name}")
    public BotOption getOption(@PathVariable String name){
        return botOptionService.getOption(name);
    }
}
