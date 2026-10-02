package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.NomeProdutoDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        Page<Produto> page;

        boolean temNome = nome != null && !nome.isBlank();
        boolean temCategoria = categoria != null;

        if (temNome && temCategoria) {
            page = repository.findByNomeContainingIgnoreCaseAndCategoria(nome, categoria, pageable);
        } else if (temNome) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (temCategoria) {
            page = repository.findByCategoria(categoria, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    @Override
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));
        return toResponseDTO(produto);
    }

    @Override
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarEstoque(dto.estoqueAtual(), dto.estoqueMinimo());

        if (repository.existsByNome(dto.nome())) {
            throw new NomeProdutoDuplicadoException(dto.nome());
        }

        Produto produto = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        Produto salvo = repository.save(produto);
        return toResponseDTO(salvo);
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));

        validarEstoque(dto.estoqueAtual(), dto.estoqueMinimo());

        if (repository.existsByNomeAndIdProdutoNot(dto.nome(), id)) {
            throw new NomeProdutoDuplicadoException(dto.nome());
        }

        produto.setNome(dto.nome());
        produto.setCategoria(dto.categoria());
        produto.setEstoqueAtual(dto.estoqueAtual());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setUnidadeMedida(dto.unidadeMedida());

        Produto salvo = repository.save(produto);
        return toResponseDTO(salvo);
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    private void validarEstoque(Integer estoqueAtual, Integer estoqueMinimo) {
        if (estoqueAtual != null && estoqueAtual < 0) {
            throw new EstoqueNegativoException("O estoque atual não pode ser negativo: " + estoqueAtual);
        }
        if (estoqueMinimo != null && estoqueMinimo < 0) {
            throw new EstoqueNegativoException("O estoque mínimo não pode ser negativo: " + estoqueMinimo);
        }
    }

    private ProdutoResponseDTO toResponseDTO(Produto entity) {
        boolean estoqueBaixo = entity.getEstoqueAtual() != null 
                && entity.getEstoqueMinimo() != null 
                && entity.getEstoqueAtual() < entity.getEstoqueMinimo();

        return new ProdutoResponseDTO(
                entity.getIdProduto(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getEstoqueAtual(),
                entity.getEstoqueMinimo(),
                entity.getUnidadeMedida(),
                entity.getDataCadastro(),
                estoqueBaixo
        );
    }
}
