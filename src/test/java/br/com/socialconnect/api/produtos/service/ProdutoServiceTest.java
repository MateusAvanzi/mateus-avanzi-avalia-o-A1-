package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.NomeProdutoDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoServiceImpl service;

    @Test
    @DisplayName("Deve criar produto com sucesso quando os dados forem válidos")
    void deveCriarProdutoQuandoDadosValidos() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz 5kg",
                CategoriaProduto.ALIMENTO,
                15,
                10,
                "pacote"
        );

        Produto produtoSalvo = Produto.builder()
                .idProduto(1L)
                .nome("Arroz 5kg")
                .categoria(CategoriaProduto.ALIMENTO)
                .estoqueAtual(15)
                .estoqueMinimo(10)
                .unidadeMedida("pacote")
                .dataCadastro(LocalDate.now())
                .build();

        Mockito.when(repository.existsByNome("Arroz 5kg")).thenReturn(false);
        Mockito.when(repository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = service.criar(dto);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.idProduto());
        Assertions.assertEquals("Arroz 5kg", resultado.nome());
        Assertions.assertFalse(resultado.estoqueBaixo(), "Estoque baixo deve ser false quando estoqueAtual >= estoqueMinimo");
        Mockito.verify(repository, Mockito.times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o estoque atual for negativo")
    void deveLancarExcecaoQuandoEstoqueNegativo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Feijão 1kg",
                CategoriaProduto.ALIMENTO,
                -5,
                10,
                "kg"
        );

        // ==========================================
        // ACT & ASSERT: Executar e verificar exceção
        // ==========================================
        Assertions.assertThrows(EstoqueNegativoException.class, () -> {
            service.criar(dto);
        });

        Mockito.verify(repository, Mockito.never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cadastrar produto com nome duplicado")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Macarrão",
                CategoriaProduto.ALIMENTO,
                20,
                10,
                "pacote"
        );

        Mockito.when(repository.existsByNome("Macarrão")).thenReturn(true);

        // ==========================================
        // ACT & ASSERT: Executar e verificar exceção
        // ==========================================
        Assertions.assertThrows(NomeProdutoDuplicadoException.class, () -> {
            service.criar(dto);
        });

        Mockito.verify(repository, Mockito.never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve definir estoqueBaixo como true quando estoqueAtual for menor que estoqueMinimo")
    void deveRetornarEstoqueBaixoTrueQuandoEstoqueAtualMenorQueMinimo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Sabonete",
                CategoriaProduto.HIGIENE,
                3,
                10,
                "unidade"
        );

        Produto produtoSalvo = Produto.builder()
                .idProduto(2L)
                .nome("Sabonete")
                .categoria(CategoriaProduto.HIGIENE)
                .estoqueAtual(3)
                .estoqueMinimo(10)
                .unidadeMedida("unidade")
                .dataCadastro(LocalDate.now())
                .build();

        Mockito.when(repository.existsByNome("Sabonete")).thenReturn(false);
        Mockito.when(repository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = service.criar(dto);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertTrue(resultado.estoqueBaixo(), "Estoque baixo deve ser true quando 3 < 10");
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso quando ele existir")
    void deveBuscarPorIdQuandoExistir() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Produto produto = Produto.builder()
                .idProduto(1L)
                .nome("Leite")
                .categoria(CategoriaProduto.ALIMENTO)
                .estoqueAtual(8)
                .estoqueMinimo(5)
                .unidadeMedida("litro")
                .dataCadastro(LocalDate.now())
                .build();

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(produto));

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = service.buscarPorId(1L);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.idProduto());
        Assertions.assertEquals("Leite", resultado.nome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar produto por ID inexistente")
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Mockito.when(repository.findById(999L)).thenReturn(Optional.empty());

        // ==========================================
        // ACT & ASSERT: Executar e verificar exceção
        // ==========================================
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.buscarPorId(999L);
        });
    }

    @Test
    @DisplayName("Deve listar produtos com paginação")
    void deveListarProdutosComPaginacao() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Produto produto = Produto.builder()
                .idProduto(1L)
                .nome("Óleo")
                .categoria(CategoriaProduto.ALIMENTO)
                .estoqueAtual(12)
                .estoqueMinimo(5)
                .unidadeMedida("litro")
                .dataCadastro(LocalDate.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Produto> page = new PageImpl<>(List.of(produto), pageable, 1);

        Mockito.when(repository.findAll(pageable)).thenReturn(page);

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        Page<ProdutoResponseDTO> resultado = service.listar(null, null, pageable);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(1, resultado.getTotalElements());
        Assertions.assertEquals("Óleo", resultado.getContent().get(0).nome());
    }
}
