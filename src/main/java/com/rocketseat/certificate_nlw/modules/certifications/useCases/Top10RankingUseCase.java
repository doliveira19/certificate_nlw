package com.rocketseat.certificate_nlw.modules.certifications.useCases;

import com.rocketseat.certificate_nlw.modules.students.entities.CertificationStudentEntity;
import com.rocketseat.certificate_nlw.modules.students.repositories.CertificateStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Top10RankingUseCase {

    @Autowired
    private CertificateStudentRepository certificateStudentRepository;
    public List<CertificationStudentEntity> execute() {
        var result = this.certificateStudentRepository.findTop10ByOrderByGradeDesc();
        return result;
    }
}
