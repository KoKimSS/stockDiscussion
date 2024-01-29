package com.kss.stockDiscussion.domain.certification;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
    Boolean isCertified ;

    @Builder
    private Certification(String email,String certificationNumber,LocalDateTime localDateTime){
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.isCertified = false;
    }

    public void certificated(){
        this.isCertified = true;
    }
}
