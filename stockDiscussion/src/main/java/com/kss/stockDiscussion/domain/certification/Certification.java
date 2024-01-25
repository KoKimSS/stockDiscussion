package com.kss.stockDiscussion.domain.certification;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification extends BaseTimeEntity {
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
