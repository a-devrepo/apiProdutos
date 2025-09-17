package br.com.nca.domain.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import br.com.nca.domain.enums.TipoProduto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObterProdutoDTO {

	private UUID id;
	private String nome;
	private TipoProduto tipo;
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal precoUnitario;
}