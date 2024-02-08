package com.rocketseat.certificate_nlw.modules.students.repositories;

import com.rocketseat.certificate_nlw.modules.students.entities.CertificateStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateStudentRepository extends JpaRepository<CertificateStudentEntity, UUID> {
    @Query("SELECT c FROM certifications c INNER JOIN c.studentEntity std WHERE std.email = :email AND c.technology = :technology")
    List<CertificateStudentEntity> findByStudentEmailAndTechnology(String email, String technology);
}
