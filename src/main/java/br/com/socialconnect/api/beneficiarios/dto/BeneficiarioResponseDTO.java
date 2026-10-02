package br.com.socialconnect.api.beneficiarios.dto;

import java.time.LocalDate;

public record BeneficiarioResponseDTO(
        Long idBeneficiario,
        String nome,
        String cpf,
        String telefone,
        String endereco,
        String situacaoVulnerabilidade,
        LocalDate dataCadastro
) {}
