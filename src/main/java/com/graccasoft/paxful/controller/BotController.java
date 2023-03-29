package com.graccasoft.paxful.controller;

import com.graccasoft.paxful.dto.GenericResponse;
import com.graccasoft.paxful.dto.OptionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {

    @PostMapping("options")
    public GenericResponse updateOption(@RequestBody OptionRequest optionRequest){

        return new GenericResponse(true, "Option successfully updates");
    }
}
