package com.rocketseat.certificate_nlw.modules.students.useCases;

import com.rocketseat.certificate_nlw.modules.students.dto.VerifyHasCertificationDTO;
import org.springframework.stereotype.Service;

@Service
public class VerifyIfHasCertificationUseCase {
    public boolean execute(VerifyHasCertificationDTO dto) {
        return dto.getEmail().equals("daniel.opa@teste.com") && dto.getTechnology().equals("JAVA");
    }
}
