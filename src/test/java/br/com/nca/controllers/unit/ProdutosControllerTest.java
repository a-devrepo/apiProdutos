package br.com.nca.controllers.unit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.nca.controllers.ProdutosController;
import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterPrecoMedioProdutoDTO;
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
		var produtoDTO = ObterProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
				
				when(produtoService.buscarPorId(id)).thenReturn(produtoDTO);
				
				mockMvc.perform(get("/api/v1/produtos/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()))
                    .andExpect(jsonPath("$.tipo").value(produtoDTO.getTipo().toString()))
                    .andExpect(jsonPath("$.precoUnitario").value(produtoDTO.getPrecoUnitario().toString()));
	}
	
	@Test
	@DisplayName("Deve retornar lista de produtos")
	public void deveRetornarListaProdutos() throws Exception {
		
		var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		
		UUID idProduto1 = UUID.randomUUID();
		UUID idProduto2 = UUID.randomUUID();
		
		
		var produtoDTO1 = ObterProdutoDTO.builder()
				.id(idProduto1)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
		
		var produtoDTO2 = ObterProdutoDTO.builder()
				.id(idProduto2)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
		
		var produtos = List.of(produtoDTO1, produtoDTO2);
		var page = new PageImpl<>(produtos);
		
				when(produtoService.listar(0,10,"nome","asc")).thenReturn(page);
				
				mockMvc.perform(get("/api/v1/produtos")
								.param("page","0")
								.param("size","10")
								.param("sortBy","nome")
								.param("direction","asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.content.[0].id").value(idProduto1.toString()))
                    .andExpect(jsonPath("$.content.[0].nome").value(produtoDTO1.getNome()))
                    .andExpect(jsonPath("$.content.[0].tipo").value(produtoDTO1.getTipo().toString()))
                    .andExpect(jsonPath("$.content.[0].precoUnitario").value(produtoDTO1.getPrecoUnitario().toString()))
                    .andExpect(jsonPath("$.content.[1].id").value(idProduto2.toString()))
                    .andExpect(jsonPath("$.content.[1].nome").value(produtoDTO2.getNome()))
                    .andExpect(jsonPath("$.content.[1].tipo").value(produtoDTO2.getTipo().toString()))
                    .andExpect(jsonPath("$.content.[1].precoUnitario").value(produtoDTO2.getPrecoUnitario().toString()))
						.andExpect(jsonPath("$.totalElements").value(2));
	}
	
	@Test
	@DisplayName("Deve cadastrar produto")
	public void deveCadastrarProduto() throws Exception {
		
		var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		UUID id = UUID.randomUUID();
		
		var criarProdutoDTO = CriarProdutoDTO.builder()
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
				.build();
				
		var obterProdutoDTO = ObterProdutoDTO.builder()
				.id(id)
				.nome(criarProdutoDTO.getNome())
				.tipo(criarProdutoDTO.getTipo())
				.precoUnitario(criarProdutoDTO.getPrecoUnitario())
						.build();
				
				when(produtoService.cadastrar(criarProdutoDTO)).thenReturn(obterProdutoDTO);
				
				var objetoJson = objectMapper.writeValueAsString(criarProdutoDTO);
				
				mockMvc.perform(post("/api/v1/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objetoJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(criarProdutoDTO.getNome()))
                    .andExpect(jsonPath("$.tipo").value(criarProdutoDTO.getTipo().toString()))
                    .andExpect(jsonPath("$.precoUnitario").value(criarProdutoDTO.getPrecoUnitario().toString()));
	}
	
	@Test
	@DisplayName("Deve alterar dados do produto")
	public void deveAlterarProduto() throws Exception {
		
		var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		UUID id = UUID.randomUUID();
		
		var alterarProdutoDTO = AlterarProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
				
		var obterProdutoDTO = ObterProdutoDTO.builder()
				.id(id)
				.nome(alterarProdutoDTO.getNome())
				.tipo(alterarProdutoDTO.getTipo())
				.precoUnitario(alterarProdutoDTO.getPrecoUnitario())
						.build();
				
				when(produtoService.alterar(alterarProdutoDTO)).thenReturn(obterProdutoDTO);
				
				var objetoJson = objectMapper.writeValueAsString(alterarProdutoDTO);
				
				mockMvc.perform(patch("/api/v1/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objetoJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(alterarProdutoDTO.getNome()))
                    .andExpect(jsonPath("$.tipo").value(alterarProdutoDTO.getTipo().toString()))
                    .andExpect(jsonPath("$.precoUnitario").value(alterarProdutoDTO.getPrecoUnitario().toString()));
	}
	
	@Test
	@DisplayName("Deve validar os campos como obrigatórios")
	public void deveValidarCamposObrigatorios() throws Exception {
		
		var criarProdutoDTO = CriarProdutoDTO.builder()
				.nome(null)
				.tipo(null)
				.precoUnitario(null)
				.build();
				
				var objetoJson = objectMapper.writeValueAsString(criarProdutoDTO);
				
				mockMvc.perform(post("/api/v1/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objetoJson))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors", hasSize(3)))
                    .andExpect(jsonPath("$.errors[*]", hasItem(containsString("Campo: 'nome' : Nome é obrigatório."))))
                    .andExpect(jsonPath("$.errors[*]", hasItem(containsString("Campo: 'tipo' : Tipo é obrigatório."))))
                    .andExpect(jsonPath("$.errors[*]", hasItem(containsString("Campo: 'precoUnitario' : Preço unitário é obrigatório."))));
	}
	
	@Test
	@DisplayName("Deve realizar exclusão lógica do produto por ID")
	public void deveRealizarExclusaoLogica() throws Exception {
		
      var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		
	  UUID id = UUID.randomUUID();
	  var produtoDTO = ObterProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
				.build();
		
	            when(produtoService.desativar(id)).thenReturn(produtoDTO);
				
				
	            mockMvc.perform(delete("/api/v1/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()))
                .andExpect(jsonPath("$.tipo").value(produtoDTO.getTipo().toString()))
                .andExpect(jsonPath("$.precoUnitario").value(produtoDTO.getPrecoUnitario().toString()));
	}
	
	@Test
	@DisplayName("Deve retornar quantidade e preço médio por tipo de produto")
	public void deveRetornarQuantiadePrecoMedioPorTipo() throws Exception {
		
      var faker = new Faker(Locale.of("pt-BR"), new Random(93));
		
	  UUID id = UUID.randomUUID();
	  var precoMedioDTO = ObterPrecoMedioProdutoDTO.builder()
	            .nome(faker.commerce().productName())
	            .tipo(TipoProduto.MATERIAL)
	            .quantidade(3)
	            .precoMedio(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
	            .build();

	    when(produtoService.obterPrecoMedioPorTipo()).thenReturn(List.of(precoMedioDTO));

	    mockMvc.perform(get("/api/v1/produtos/preco-medio")
	            .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$[0].nome").value(precoMedioDTO.getNome()))
	        .andExpect(jsonPath("$[0].tipo").value(precoMedioDTO.getTipo().toString()))
	        .andExpect(jsonPath("$[0].quantidade").value(3))
	        .andExpect(jsonPath("$[0].precoMedio").value(precoMedioDTO.getPrecoMedio().toString()));
	}
}