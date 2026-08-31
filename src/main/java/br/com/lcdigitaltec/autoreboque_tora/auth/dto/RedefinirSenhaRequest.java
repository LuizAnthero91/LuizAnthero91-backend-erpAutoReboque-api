package br.com.lcdigitaltec.autoreboque_tora.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequest(
   @NotBlank
   String token,

   @NotBlank
   @Size(min = 8,max = 100)
   String novaSenha,

   @NotBlank
   String confirmarSenha
) {}



