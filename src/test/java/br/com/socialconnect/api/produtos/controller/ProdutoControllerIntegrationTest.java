package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ProdutoControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (postgres.isRunning()) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
            registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
            registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar produto com dados válidos e retornar 201 Created")
    void deveCriarProdutoQuandoDadosValidos() throws Exception {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Arroz Branco 5kg",
                CategoriaProduto.ALIMENTO,
                20,
                5,
                "pacote"
        );

        // ==========================================
        // ACT & ASSERT: Executar o POST e verificar retorno 201 Created
        // ==========================================
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.idProduto").isNumber())
                .andExpect(jsonPath("$.nome").value("Arroz Branco 5kg"))
                .andExpect(jsonPath("$.estoqueBaixo").value(false));
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict quando tentar cadastrar produto com nome duplicado")
    void deveRetornar409QuandoNomeDuplicado() throws Exception {
        // ==========================================
        // ARRANGE: Preparar o cenário com produto pré-existente
        // ==========================================
        ProdutoRequestDTO produtoInicial = new ProdutoRequestDTO(
                "Feijão Preto 1kg",
                CategoriaProduto.ALIMENTO,
                10,
                3,
                "pacote"
        );
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoInicial)))
                .andExpect(status().isCreated());

        ProdutoRequestDTO produtoDuplicado = new ProdutoRequestDTO(
                "Feijão Preto 1kg",
                CategoriaProduto.ALIMENTO,
                15,
                5,
                "pacote"
        );

        // ==========================================
        // ACT & ASSERT: Executar POST com nome repetido e verificar 409 Conflict
        // ==========================================
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDuplicado)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Deve retornar 422 Unprocessable Entity quando o estoque for negativo")
    void deveRetornar422QuandoEstoqueNegativo() throws Exception {
        // ==========================================
        // ARRANGE: Preparar o cenário com estoque negativo
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "Detergente Líquido",
                CategoriaProduto.HIGIENE,
                -10,
                5,
                "unidade"
        );

        // ==========================================
        // ACT & ASSERT: Executar o POST e verificar 422 Unprocessable Entity
        // ==========================================
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }
}
