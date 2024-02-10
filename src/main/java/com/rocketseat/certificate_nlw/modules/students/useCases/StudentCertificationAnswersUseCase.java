package com.rocketseat.certificate_nlw.modules.students.useCases;

import com.rocketseat.certificate_nlw.modules.questions.entities.QuestionEntity;
import com.rocketseat.certificate_nlw.modules.questions.repositories.QuestionRepository;
import com.rocketseat.certificate_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.rocketseat.certificate_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certificate_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.rocketseat.certificate_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certificate_nlw.modules.students.entities.StudentEntity;
import com.rocketseat.certificate_nlw.modules.students.repositories.CertificateStudentRepository;
import com.rocketseat.certificate_nlw.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificateStudentRepository certificateStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

        if (hasCertification) {
            throw new Exception("Você já tirou sua certificação!");
        }

        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswersCount = new AtomicInteger(0);

        dto.getQuestionsAnswers()
                .stream().forEach(questionAnswer -> {
                    var question = questionsEntity.stream()
                            .filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();

                    var foundCorrectAlternative = question.getAlternatives().stream()
                            .filter(alternative -> alternative.isCorrect()).findFirst().get();

                    if (foundCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                        questionAnswer.setCorrect(true);
                        correctAnswersCount.incrementAndGet();
                    } else {
                        questionAnswer.setCorrect(false);
                    }

                    var answersCertificationsEntity = AnswersCertificationsEntity.builder()
                            .answerId(questionAnswer.getAlternativeID())
                            .questionId(questionAnswer.getQuestionID())
                            .isCorrect(questionAnswer.isCorrect())
                            .build();

                    answersCertifications.add(answersCertificationsEntity);
                });

        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()) {
            var studentCreated = StudentEntity.builder()
                    .email(dto.getEmail())
                    .build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                .grade(correctAnswersCount.get())
                .build();

        var certificationStudentCreated = certificateStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answerCertification -> {
            answerCertification.setCertificationId(certificationStudentEntity.getId());
            answerCertification.setCertificateStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationsEntities(answersCertifications);

        certificateStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;

    }
}
