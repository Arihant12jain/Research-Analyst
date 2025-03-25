package com.example.Research.Analyst;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/text")
public class REsearchController {
    @Autowired
private ResearchService Research;
@RequestMapping("/generate")
public Mono<Map<String, String>> generate(@RequestBody TextRes textres) {
    return Research.generate(textres)
            .map(summary -> Collections.singletonMap("summary", summary));
}
}

