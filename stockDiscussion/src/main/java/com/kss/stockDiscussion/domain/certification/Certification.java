package com.kss.stockDiscussion.domain.certification;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification {
    @Id
    @GeneratedValue
    @Column(name = "certification_id")
    Long id;

    String email;
    String certificationNumber;
    @Builder
    private Certification(String email,String certificationNumber){
        this.email = email;
        this.certificationNumber = certificationNumber;
    }
}
