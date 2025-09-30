package br.com.nca.domain.repositories;

import br.com.nca.domain.enums.TipoProduto;

import java.math.BigDecimal;

public interface PrecoMedioProjection {
    String getNome();
    TipoProduto getTipo();
    Integer getQuantidade();
    BigDecimal getPrecoMedio();
}