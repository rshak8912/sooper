package com.simgok.sooper.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Item {
    @Id  @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(unique = true)
    private String path;

}
