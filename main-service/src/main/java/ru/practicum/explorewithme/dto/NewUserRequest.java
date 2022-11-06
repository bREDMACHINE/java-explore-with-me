package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @Email
    private String email;
    @NotBlank
    private String name;
}
