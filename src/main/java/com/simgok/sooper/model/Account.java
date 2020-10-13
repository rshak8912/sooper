package com.simgok.sooper.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String nickname;

    private String password;

    private String email;

    private String location;

    private String detailsLocation;

    @Enumerated(value = EnumType.STRING)
    private Role role;


}
