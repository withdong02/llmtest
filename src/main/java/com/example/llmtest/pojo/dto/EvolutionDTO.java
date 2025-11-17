package com.example.llmtest.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EvolutionDTO {
    private List<Long> dataIds;
    private String transformationType;
}
