package com.simgok.sooper.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "orders")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String nickname;

    private String password;

    private String email;

    private String location;

    private String detailsLocation;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "account")
    private List<Order> orders = new ArrayList<>();
}
