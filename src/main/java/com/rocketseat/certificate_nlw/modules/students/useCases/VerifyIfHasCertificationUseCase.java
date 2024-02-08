package com.rocketseat.certificate_nlw.modules.students.useCases;

import com.rocketseat.certificate_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.rocketseat.certificate_nlw.modules.students.repositories.CertificateStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyIfHasCertificationUseCase {

    @Autowired
    private CertificateStudentRepository certificateStudentRepository;
    public boolean execute(VerifyHasCertificationDTO dto) {
        var result = this.certificateStudentRepository.findByStudentEmailAndTechnology(dto.getEmail(), dto.getTechnology());
        return (!result.isEmpty());
    }
}
