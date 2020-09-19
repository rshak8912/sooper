package com.simgok.sooper.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String password;

    private String email;

    private String location;

    private String detailsLocation;

    @Enumerated(value = EnumType.STRING)
    private Role role;


}
