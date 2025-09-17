package br.com.nca.controllers.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.nca.controllers.ProdutosController;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.enums.TipoProduto;
import br.com.nca.domain.services.ProdutoService;

@AutoConfigureMockMvc
@DisplayName("Testes unitários para ProdutosController")
@WebMvcTest(ProdutosController.class)
public class ProdutosControllerTest {

	@Autowired
	private MockMvc mockMvc;
	 
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private ProdutoService produtoService;
	
	@Test
	@DisplayName("Deve retornar produto por ID")
	public void deveRetornarProdutoPorId() throws Exception {
		
		var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		
		UUID id = UUID.randomUUID();
		var produto = ObterProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
				
				when(produtoService.buscarPorId(id)).thenReturn(produto);
				
				mockMvc.perform(get("/api/v1/produtos/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(produto.getNome()))
                    .andExpect(jsonPath("$.tipo").value(produto.getTipo().toString()))
                    .andExpect(jsonPath("$.precoUnitario").value(produto.getPrecoUnitario().toString()));
	}
}
