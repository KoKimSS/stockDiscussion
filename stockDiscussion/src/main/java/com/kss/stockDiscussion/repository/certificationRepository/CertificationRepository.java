package com.kss.stockDiscussion.repository.certificationRepository;

import com.kss.stockDiscussion.domain.certification.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification,Long> {
}
