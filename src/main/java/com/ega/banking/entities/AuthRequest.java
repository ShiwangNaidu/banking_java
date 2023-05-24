package com.ega.banking.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotNull
    @Email
    @Length(min = 5, max = 50)
    private String email;

    @NotNull
    @Length(min = 5, max = 10)
    private String password;

    // getters and setters are not shown...
}
