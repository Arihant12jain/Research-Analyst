package com.example.Research.Analyst;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GEmini {

    public List<Candidate> candidates;



    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Candidate{
        private Content content;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)

public static class Content{
        private List<Part> parts;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown=true)

    public static class Part{
        private String text;
    }
}
