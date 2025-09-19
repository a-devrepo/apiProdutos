package br.com.nca.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.nca.domain.enums.TipoProduto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tb_produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
    @Column(name = "nome", length = 100 ,nullable = false)
	private String nome;
	
    @Column(name = "tipo", nullable = false)
	private TipoProduto tipo;
	
    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false)
	private BigDecimal precoUnitario;
	
    @Column(name = "ativo", nullable = false)
	private Boolean ativo;
}
