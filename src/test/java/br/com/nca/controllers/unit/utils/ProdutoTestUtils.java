package br.com.nca.controllers.unit.utils;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import com.github.javafaker.Faker;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
import br.com.nca.domain.enums.TipoProduto;

public class ProdutoTestUtils {

    private static final Faker faker = new Faker(Locale.of("pt-BR"), new Random(93));

    public static Produto getProdutoEntity(ObterProdutoDTO obterProdutoDTO) {
        return Produto.builder()
                .id(obterProdutoDTO.getId())
                .nome(obterProdutoDTO.getNome())
                .tipo(obterProdutoDTO.getTipo())
                .precoUnitario(obterProdutoDTO.getPrecoUnitario())
                .ativo(true)
                .build();
    }

    public static Produto getProdutoEntity(UUID id) {
        return Produto.builder()
                .id(id)
                .nome(faker.commerce().productName())
                .tipo(TipoProduto.MATERIAL)
                .precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
                .ativo(true)
                .build();
    }

    public static Produto getProdutoEntity(CriarProdutoDTO criarProdutoDTO) {
        return Produto.builder()
                .nome(criarProdutoDTO.getNome())
                .tipo(criarProdutoDTO.getTipo())
                .precoUnitario(criarProdutoDTO.getPrecoUnitario())
                .ativo(true)
                .build();
    }

    public static Produto getProdutoCadastrado(Produto produto) {
        return Produto.builder()
                .id(UUID.randomUUID())
                .nome(produto.getNome())
                .tipo(produto.getTipo())
                .precoUnitario(produto.getPrecoUnitario())
                .ativo(true)
                .build();
    }

    public static CriarProdutoDTO getCriarProdutoDTO() {
        return CriarProdutoDTO.builder()
                .nome(faker.commerce().productName())
                .tipo(TipoProduto.MATERIAL)
                .precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
                .build();
    }

    public static AlterarProdutoDTO getAlterarProdutoDTO() {
        return AlterarProdutoDTO.builder()
                .id(UUID.randomUUID())
                .nome(faker.commerce().productName())
                .tipo(TipoProduto.MATERIAL)
                .precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
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
