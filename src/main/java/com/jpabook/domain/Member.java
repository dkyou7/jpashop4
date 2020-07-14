package com.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;


    private String name;

    @Embedded
    private Address address;

    // 연관관계의 종속자
    @OneToMany(mappedBy = "member") // 반대쪽 변수 명을 적는다.
    private List<Order> orders = new ArrayList<>();
}
