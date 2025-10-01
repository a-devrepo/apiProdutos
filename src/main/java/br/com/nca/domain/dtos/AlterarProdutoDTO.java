package br.com.nca.domain.dtos;

import br.com.nca.domain.enums.TipoProduto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class AlterarProdutoDTO {

    @NotNull(message = "O id é obrigatório")
    private UUID id;

    @Size(min = 6, max = 100, message = "Nome deve ter entre 5 e 100 caracteres.")
    @Pattern(regexp = "^[A-Za-zÀ-ú0-9 ]+$", message = "Nome deve conter apenas letras, números e espaços.")
    private String nome;

    private TipoProduto tipo;

    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal precoUnitario;
}
