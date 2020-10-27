package com.simgok.sooper.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Profile {

    @Length(min = 3, max = 20)
    private String location;

    @Length(min = 3, max = 20)
    private String detailsLocation;

    @Email
    @NotBlank
    private String email;

  /*  public Profile(Account account) {

        this.location = account.getLocation();
        this.detailsLocation = account.getDetailsLocation();
        this.email = account.getEmail();
    }*/
}
