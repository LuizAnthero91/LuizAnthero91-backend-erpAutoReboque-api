package br.com.lcdigitaltec.autoreboque_tora.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaRequest(

        @NotBlank
        @Email
        String email
) {}

