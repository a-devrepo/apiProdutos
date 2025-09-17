package br.com.nca.controllers.unit;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@DisplayName("Testes unitários para ProdutosController")
@WebMvcTest(ProdutosController.class)
public class ProdutosControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ProdutoService produtoService;
	
	@Test
	@DisplayName("Deve retornar produto por ID")
	public void deveRetornarProdutoPorId() {
		
		var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		
		UUID id = UUID.randomUUID();
		var produto = ObterProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0))
						.build();
				
				when(produtoService.buscarPorId(id)).thenReturn(produto);
				
				mockMvc.perform(get("/api/v1/produtos/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(produto.getNome()))
                    .andExpect(jsonPath("$.tipo").value(produto.getTipo().toString()))
                    .andExpect(jsonPath("$.precoUnitario").value(produto.getPrecoUnitario()));
	}
	
}
