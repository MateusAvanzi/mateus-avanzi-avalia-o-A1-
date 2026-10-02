package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.validation.UnidadeMedidaValida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(
    @Schema(description = "Nome único do produto", example = "Arroz 5kg")
    @NotBlank(message = "{produto.nome.obrigatorio}")
    @Size(max = 150, message = "{produto.nome.tamanho}")
    String nome,

    @Schema(description = "Categoria do produto", example = "ALIMENTO")
    @NotNull(message = "{produto.categoria.obrigatoria}")
    CategoriaProduto categoria,

    @Schema(description = "Quantidade atual em estoque", example = "3")
    @NotNull(message = "{produto.estoque.atual.obrigatorio}")
    Integer estoqueAtual,

    @Schema(description = "Quantidade mínima de segurança para o estoque", example = "10")
    @NotNull(message = "{produto.estoque.minimo.obrigatorio}")
    Integer estoqueMinimo,

    @Schema(description = "Unidade de medida do produto (ex: unidade, kg, litro, pacote, caixa)", example = "unidade")
    @NotBlank(message = "{produto.unidade.medida.obrigatoria}")
    @Size(max = 20, message = "{produto.unidade.medida.tamanho}")
    @UnidadeMedidaValida
    String unidadeMedida
) {}
