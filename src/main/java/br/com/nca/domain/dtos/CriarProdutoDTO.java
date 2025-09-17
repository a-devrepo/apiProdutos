package br.com.nca.domain.dtos;

import java.math.BigDecimal;

import br.com.nca.domain.enums.TipoProduto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriarProdutoDTO {
	
	private String nome;
	private TipoProduto tipo;
	private BigDecimal precoUnitario;
}