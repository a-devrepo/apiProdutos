package br.com.nca.domain.services;

import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
import br.com.nca.domain.enums.TipoProduto;
import br.com.nca.domain.exceptions.RecursoNaoEncontradoException;
import br.com.nca.domain.repositories.PrecoMedioProjection;
import br.com.nca.domain.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.nca.utils.ProdutoTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testes unitários para ProdutoService")
@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        produtoService = new ProdutoServiceImpl(produtoRepository, modelMapper);
    }

    @Test
    @DisplayName("Deve retornar produto por ID")
    public void deveRetornarProdutoPorId() throws Exception {

        var id = UUID.randomUUID();
        var produtoEntity = getProdutoEntity(id);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);


        when(produtoRepository.findByIdAndAtivoTrue(id)).thenReturn(Optional.of(produtoEntity));
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);

        var resultado = produtoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(resultado.getNome(), produtoEntity.getNome());
    }

    @Test
    @DisplayName("Deve retornar page com produtos")
    public void deveRetornarPageProdutos() throws Exception {

        UUID id = UUID.randomUUID();
        var produtoEntity = getProdutoEntity(id);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

        var pageRequest = PageRequest.of(0, 10,Sort.by(Sort.Direction.ASC, "nome"));
        var page = new PageImpl<>(List.of(produtoEntity), pageRequest,1);

        when(produtoRepository.findByAtivoTrue(pageRequest)).thenReturn(page);
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);

        var resultado = produtoService.listar(0,10,"nome","asc");

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(obterProdutoDTO.getId(), resultado.getContent().getFirst().getId());
        assertEquals(obterProdutoDTO.getNome(), resultado.getContent().getFirst().getNome());
        assertEquals(obterProdutoDTO.getPrecoUnitario(), resultado.getContent().getFirst().getPrecoUnitario());
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

        var produtoEntity = getProdutoEntity(UUID.randomUUID());
        var criarProdutoDTO = getCriarProdutoDTO(produtoEntity);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

        when(modelMapper.map(criarProdutoDTO, Produto.class)).thenReturn(produtoEntity);
        when(produtoRepository.save(produtoEntity)).thenReturn(produtoEntity);
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);

        var resultado = produtoService.cadastrar(criarProdutoDTO);

        assertNotNull(resultado);
        assertEquals(criarProdutoDTO.getNome(), obterProdutoDTO.getNome());
    }

    @Test
    @DisplayName("Deve alterar produto com sucesso")
    public void deveAlterarProdutoComSucesso() throws Exception {

        var produtoEntity = getProdutoEntity(UUID.randomUUID());
        var alterarProdutoDTO = getAlterarProdutoDTO(produtoEntity);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

        when(produtoRepository.findByIdAndAtivoTrue(any(UUID.class))).thenReturn(Optional.of(produtoEntity));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoEntity);
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);

        var resultado = produtoService.alterar(alterarProdutoDTO);

        assertNotNull(resultado);
        assertEquals(alterarProdutoDTO.getNome(), obterProdutoDTO.getNome());
    }

    @Test
    @DisplayName("Deve excluir produto com sucesso")
    public void deveExcluirProdutoComSucesso() throws Exception {

        var id = UUID.randomUUID();
        var produtoEntity = getProdutoEntity(id);

        when(produtoRepository.findByIdAndAtivoTrue(any(UUID.class))).thenReturn(Optional.of(produtoEntity));

        produtoEntity.setAtivo(false);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoEntity);
        when(modelMapper.map(produtoEntity, ObterProdutoDTO.class)).thenReturn(obterProdutoDTO);

        var resultado = produtoService.desativar(id);

        assertNotNull(resultado);
        assertEquals(id, obterProdutoDTO.getId());
    }

    @Test
    @DisplayName("Deve retornar preço médio de produtos por tipo")
    public void deveRetornarPrecoMedioPorTipo() throws Exception {

        PrecoMedioProjection projection1 = Mockito.mock(PrecoMedioProjection.class);
        PrecoMedioProjection projection2 = Mockito.mock(PrecoMedioProjection.class);

        when(projection1.getNome()).thenReturn("Caderno");
        when(projection1.getTipo()).thenReturn(TipoProduto.MATERIAL);
        when(projection1.getQuantidade()).thenReturn(3);
        when(projection1.getPrecoMedio()).thenReturn(new BigDecimal("15.50"));

        when(projection2.getNome()).thenReturn("Lápis");
        when(projection2.getTipo()).thenReturn(TipoProduto.MATERIAL);
        when(projection2.getQuantidade()).thenReturn(5);
        when(projection2.getPrecoMedio()).thenReturn(new BigDecimal("2.30"));

        when(produtoRepository.obterPrecoMedioPorTipo())
                .thenReturn(List.of(projection1, projection2));

        var resultado = produtoService.obterPrecoMedioPorTipo();

        assertThat(resultado).hasSize(2);

        assertThat(resultado.get(0).getNome()).isEqualTo("Caderno");
        assertThat(resultado.get(0).getTipo()).isEqualTo(TipoProduto.MATERIAL);
        assertThat(resultado.get(0).getQuantidade()).isEqualTo(3);
        assertThat(resultado.get(0).getPrecoMedio()).isEqualByComparingTo("15.50");

        assertThat(resultado.get(1).getNome()).isEqualTo("Lápis");
        assertThat(resultado.get(1).getTipo()).isEqualTo(TipoProduto.MATERIAL);
        assertThat(resultado.get(1).getQuantidade()).isEqualTo(5);
        assertThat(resultado.get(1).getPrecoMedio()).isEqualByComparingTo("2.30");

        verify(produtoRepository, times(1)).obterPrecoMedioPorTipo();
    }
}