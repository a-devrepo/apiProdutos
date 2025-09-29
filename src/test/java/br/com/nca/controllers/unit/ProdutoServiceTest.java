package br.com.nca.controllers.unit;

import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
import br.com.nca.domain.exceptions.RecursoNaoEncontradoException;
import br.com.nca.domain.repositories.ProdutoRepository;
import br.com.nca.domain.services.ProdutoService;
import br.com.nca.domain.services.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static br.com.nca.controllers.unit.utils.ProdutoTestUtils.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}