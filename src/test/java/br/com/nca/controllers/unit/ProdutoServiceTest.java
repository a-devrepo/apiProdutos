package br.com.nca.controllers.unit;

import static br.com.nca.controllers.unit.utils.ProdutoTestUtils.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import br.com.nca.controllers.unit.utils.ProdutoTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.github.javafaker.Faker;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
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
		
		var produtoEntity = getProdutoEntity(obterProdutoDTO);
				
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
	
	@Test
	@DisplayName("Deve salvar produto com sucesso")
	public void deveSalvarProdutoComSucesso() throws Exception {
		
		var criarProdutoDTO = getCriarProdutoDTO();
		var produtoEntity = getProdutoEntity(criarProdutoDTO);
		var produtoCadastrado = getProdutoCadastrado(produtoEntity);
		var obterProdutoDTO = getObterProdutoDTO(produtoCadastrado);	
		
		when(modelMapper.map(criarProdutoDTO,Produto.class)).thenReturn(produtoEntity);
        when(produtoRepository.save(produtoEntity)).thenReturn(produtoCadastrado);
        when(modelMapper.map(produtoCadastrado, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);
        
		var resultado = produtoService.cadastrar(criarProdutoDTO);
				
		assertNotNull(resultado);
		assertEquals(criarProdutoDTO.getNome(), obterProdutoDTO.getNome());
	}
	
	@Test
	@DisplayName("Deve alterar produto com sucesso")
	public void deveAlterarProdutoComSucesso() throws Exception {
		
		var produtoEntity = ProdutoTestUtils.getProdutoEntity(UUID.randomUUID());
		var alterarProdutoDTO = ProdutoTestUtils.getAlterarProdutoDTO(produtoEntity);
		var obterProdutoDTO = ProdutoTestUtils.getObterProdutoDTO(produtoEntity);
		
        when(produtoRepository.findByIdAndAtivoTrue(any(UUID.class))).thenReturn(Optional.of(produtoEntity));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoEntity);
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);
        
		var resultado = produtoService.alterar(alterarProdutoDTO);
				
		assertNotNull(resultado);
		assertEquals(alterarProdutoDTO.getNome(), obterProdutoDTO.getNome());
	}

	private Produto getProdutoEntity(ObterProdutoDTO obterProdutoDTO) {
		var produtoEntity = Produto.builder().id(obterProdutoDTO.getId())
				.nome(obterProdutoDTO.getNome())
				.tipo(obterProdutoDTO.getTipo())
				.precoUnitario(obterProdutoDTO.getPrecoUnitario())
				.ativo(true)
				.build();
		return produtoEntity;
	}
	
	private Produto getProdutoEntity(UUID id) {
		var produtoEntity = Produto.builder().id(id)
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(
						new BigDecimal(
								faker.commerce().price(10.0, 500.0).replace(",", ".")))
				.ativo(true)
				.build();
		return produtoEntity;
	}
	
	private ObterProdutoDTO getObterProdutoDTO(Produto produto) {
		
		var obterProdutoDTO = ObterProdutoDTO.builder()
				.id(produto.getId())
				.nome(produto.getNome())
				.tipo(produto.getTipo())
				.precoUnitario(produto.getPrecoUnitario())
				.build();
		return obterProdutoDTO;
	}
	
    private CriarProdutoDTO getCriarProdutoDTO() {
		
		var criarProdutoDTO = CriarProdutoDTO.builder()
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
				.build();
		return criarProdutoDTO;
	}
    
    private AlterarProdutoDTO getAlterarProdutoDTO() {
		
		var alterarProdutoDTO = AlterarProdutoDTO.builder()
				.id(UUID.randomUUID())
				.nome(faker.commerce().productName())
				.tipo(TipoProduto.MATERIAL)
				.precoUnitario(new BigDecimal(faker.commerce().price(10.0, 500.0).replace(",", ".")))
				.build();
		return alterarProdutoDTO;
	}
	
	private Produto getProdutoEntity(CriarProdutoDTO criarProdutoDTO) {
		
		var produtoEntity = Produto.builder()
				.nome(criarProdutoDTO.getNome())
				.tipo(criarProdutoDTO.getTipo())
				.precoUnitario(criarProdutoDTO.getPrecoUnitario())
				.ativo(true)
				.build();
		return produtoEntity;
	}
	
    private Produto getProdutoCadastrado(Produto produto) {
		
		var produtoEntity = Produto.builder()
				.id(UUID.randomUUID())
				.nome(produto.getNome())
				.tipo(produto.getTipo())
				.precoUnitario(produto.getPrecoUnitario())
				.ativo(true)
				.build();
		return produtoEntity;
	}
}
