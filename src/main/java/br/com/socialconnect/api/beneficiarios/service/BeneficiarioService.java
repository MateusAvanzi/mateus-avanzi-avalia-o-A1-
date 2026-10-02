package br.com.socialconnect.api.beneficiarios.service;

import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioPatchDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioRequestDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioResponseDTO;
import br.com.socialconnect.api.beneficiarios.model.Beneficiario;
import br.com.socialconnect.api.beneficiarios.repository.BeneficiarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BeneficiarioService {

    private final BeneficiarioRepository repository;

    // Injeção de dependência via construtor (Boa prática para testes)
    public BeneficiarioService(BeneficiarioRepository repository) {
        this.repository = repository;
    }

    public Page<BeneficiarioResponseDTO> listar(String nome, String cpf, Pageable pageable) {
        Page<Beneficiario> page;

        if (cpf != null && !cpf.isBlank()) {
            page = repository.findByCpf(cpf, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDTO);
    }

    public BeneficiarioResponseDTO buscarPorId(Long idBeneficiario) {
        Beneficiario beneficiario = repository.findById(idBeneficiario)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado com o ID: " + idBeneficiario));
        return toDTO(beneficiario);
    }

    public BeneficiarioResponseDTO criar(BeneficiarioRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new RuntimeException("CPF já cadastrado: " + dto.cpf());
        }

        Beneficiario beneficiario = Beneficiario.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .situacaoVulnerabilidade(dto.situacaoVulnerabilidade())
                .dataCadastro(LocalDate.now())
                .build();

        return toDTO(repository.save(beneficiario));
    }

    public BeneficiarioResponseDTO atualizar(Long idBeneficiario, BeneficiarioRequestDTO dto) {
        Beneficiario beneficiario = repository.findById(idBeneficiario)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado com o ID: " + idBeneficiario));

        beneficiario.setNome(dto.nome());
        beneficiario.setCpf(dto.cpf());
        beneficiario.setTelefone(dto.telefone());
        beneficiario.setEndereco(dto.endereco());
        beneficiario.setSituacaoVulnerabilidade(dto.situacaoVulnerabilidade());

        return toDTO(repository.save(beneficiario));
    }

    public BeneficiarioResponseDTO atualizarParcial(Long idBeneficiario, BeneficiarioPatchDTO dto) {
        Beneficiario beneficiario = repository.findById(idBeneficiario)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado com o ID: " + idBeneficiario));

        if (dto.nome() != null) beneficiario.setNome(dto.nome());
        if (dto.telefone() != null) beneficiario.setTelefone(dto.telefone());
        if (dto.endereco() != null) beneficiario.setEndereco(dto.endereco());
        if (dto.situacaoVulnerabilidade() != null) beneficiario.setSituacaoVulnerabilidade(dto.situacaoVulnerabilidade());

        return toDTO(repository.save(beneficiario));
    }

    public void deletar(Long idBeneficiario) {
        repository.deleteById(idBeneficiario);
    }

    private BeneficiarioResponseDTO toDTO(Beneficiario entity) {
        return new BeneficiarioResponseDTO(
                entity.getIdBeneficiario(), entity.getNome(), entity.getCpf(),
                entity.getTelefone(), entity.getEndereco(), entity.getSituacaoVulnerabilidade(),
                entity.getDataCadastro()
        );
    }
}
