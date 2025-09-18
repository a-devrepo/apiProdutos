package br.com.nca.domain.dtos;

import java.math.BigDecimal;

import br.com.nca.domain.enums.TipoProduto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriarProdutoDTO {
	
	@NotBlank(message = "Nome é obrigatório.")
	@Size(min = 6, max = 100, message = "Nome deve ter entre 5 e 100 caracteres.")
	@Pattern(regexp = "^[A-Za-zÀ-ú0-9 ]+$", message = "Nome deve conter apenas letras, números e espaços.")
	private String nome;
	
	@NotBlank(message = "Tipo é obrigatório.")
	private TipoProduto tipo;
	
	@NotNull(message = "Preço unitário é obrigatório.")
	@DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
	private BigDecimal precoUnitario;
}