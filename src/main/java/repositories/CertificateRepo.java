package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import entities.Certificate;

public interface CertificateRepo extends JpaRepository<Certificate, Long>{

	Certificate findByUserEmailAndCourseName(
            String userEmail,
            String courseName);
}