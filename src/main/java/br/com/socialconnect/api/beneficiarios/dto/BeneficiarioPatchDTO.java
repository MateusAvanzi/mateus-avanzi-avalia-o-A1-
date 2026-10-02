package br.com.socialconnect.api.beneficiarios.dto;

public record BeneficiarioPatchDTO(
        String nome,
        String telefone,
        String endereco,
        String situacaoVulnerabilidade
) {}
