package br.com.nca.controllers.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.github.javafaker.Faker;

import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
import br.com.nca.domain.enums.TipoProduto;
import br.com.nca.domain.exceptions.RecursoNaoEncontradoException;
import br.com.nca.domain.repositories.ProdutoRepository;
import br.com.nca.domain.services.ProdutoService;
import br.com.nca.domain.services.ProdutoServiceImpl;

@DisplayName("Testes unitários para ProdutoService")
@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

	private ProdutoService produtoService;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	private Faker faker;
	
	@BeforeEach
	public void setup() {
		faker = new Faker(Locale.of("pt-BR"), new Random(93));
		produtoService = new ProdutoServiceImpl(produtoRepository, modelMapper);
	}
	
	@Test
	@DisplayName("Deve retornar produto por ID")
	public void deveRetornarProdutoPorId() throws Exception {
		
		UUID id = UUID.randomUUID();
		var obterProdutoDTO = ObterProdutoDTO.builder()
				.id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
						.build();
		
		var produtoEntity = Produto.builder().id(obterProdutoDTO.getId())
				.nome(obterProdutoDTO.getNome())
				.tipo(obterProdutoDTO.getTipo())
				.precoUnitario(obterProdutoDTO.getPrecoUnitario())
				.ativo(true)
				.build();
				
				when(produtoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.ofNullable(produtoEntity));
				when(modelMapper.map(produtoEntity,ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);
				
				var resultado = produtoService.buscarPorId(id);
				
				assertNotNull(resultado);
				assertEquals(resultado.getNome(), produtoEntity.getNome());
	}

	@Test
	@DisplayName("Deve lançar Exception quando produto não for encontrado")
	public void deveLancarExceptionQuandoProdutoNaoEncontrado() throws Exception {
		
		UUID id = UUID.randomUUID();
		
		when(produtoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.ofNullable(null));
				
		assertThatThrownBy(() -> produtoService
                .buscarPorId(id))
        .isInstanceOf(RecursoNaoEncontradoException.class);		
	}
}
