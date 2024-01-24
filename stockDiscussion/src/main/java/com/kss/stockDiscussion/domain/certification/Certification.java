package com.kss.stockDiscussion.domain.certification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification {
    @Id
    @GeneratedValue
    Long id;

    String email;
    String certificationNumber;
    @Builder
    private Certification(String email,String certificationNumber){
        this.email = email;
        this.certificationNumber = certificationNumber;
    }
}
