package com.stadium.mystadium.dao;

import com.stadium.mystadium.validator.OnlyLetters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FanDao {

    @OnlyLetters
    private String name;

    @NotBlank(message = "The email must be defined!")
    @Email(message = "The email should have a valid format!")
    private String email;
}
