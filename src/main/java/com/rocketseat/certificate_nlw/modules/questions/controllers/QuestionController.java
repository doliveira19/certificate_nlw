package com.rocketseat.certificate_nlw.modules.questions.controllers;

import com.rocketseat.certificate_nlw.modules.questions.dto.AlternativesResultDTO;
import com.rocketseat.certificate_nlw.modules.questions.dto.QuestionResultDTO;
import com.rocketseat.certificate_nlw.modules.questions.entities.AlternativesEntity;
import com.rocketseat.certificate_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certificate_nlw.modules.questions.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    @GetMapping("/technology/{technology}")
    public List<QuestionResultDTO> findByTechnology(@PathVariable String technology) {
        var result = this.questionRepository.findByTechnology(technology);
        return result.stream().map(question -> mapQuestionToDTO(question))
                .collect(Collectors.toList());
    }

    static QuestionResultDTO mapQuestionToDTO(QuestionEntity question) {
        var questionResultDTO = QuestionResultDTO.builder()
                .id(question.getId())
                .description(question.getDescription())
                .technology(question.getTechnology())
                .build();

        List<AlternativesResultDTO> alternativesResultDTO = question.getAlternatives()
                .stream().map(alternative -> mapAlternativeToDTO(alternative))
                .collect(Collectors.toList());

        questionResultDTO.setAlternatives(alternativesResultDTO);
        return questionResultDTO;
    }

    static AlternativesResultDTO mapAlternativeToDTO(AlternativesEntity alternatives) {
        return AlternativesResultDTO.builder()
                .id(alternatives.getId())
                .description(alternatives.getDescription())
                .build();
    }
}
