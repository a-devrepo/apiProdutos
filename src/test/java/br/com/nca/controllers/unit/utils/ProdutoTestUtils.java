package br.com.nca.controllers.unit.utils;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
import br.com.nca.domain.enums.TipoProduto;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class ProdutoTestUtils {

    private static final Faker faker = new Faker(Locale.of("pt-BR"), new Random(93));

    public static Produto getProdutoEntity(UUID id) {
        return Produto.builder()
                .id(id)
                .nome(faker.commerce().productName())
                .tipo(TipoProduto.MATERIAL)
                .precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
                .ativo(true)
                .build();
    }

    public static CriarProdutoDTO getCriarProdutoDTO(Produto produto) {
        return CriarProdutoDTO.builder()
                .nome(produto.getNome())
                .tipo(produto.getTipo())
                .precoUnitario(produto.getPrecoUnitario())
                .build();
    }

    public static AlterarProdutoDTO getAlterarProdutoDTO(Produto produto) {
        return AlterarProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .tipo(produto.getTipo())
                .precoUnitario(produto.getPrecoUnitario())
                .build();
    }

    public static ObterProdutoDTO getObterProdutoDTO(Produto produto) {
        return ObterProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .tipo(produto.getTipo())
                .precoUnitario(produto.getPrecoUnitario())
                .build();
    }
}