package br.com.nca.controllers.unit;

import br.com.nca.controllers.ProdutosController;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.exceptions.RecursoNaoEncontradoException;
import br.com.nca.domain.services.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static br.com.nca.controllers.unit.utils.ProdutoTestUtils.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        UUID id = UUID.randomUUID();
        var produtoDTO = getObterProdutoDTO(getProdutoEntity(id));

        when(produtoService.buscarPorId(id)).thenReturn(produtoDTO);

        mockMvc.perform(get("/api/v1/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value(produtoDTO.getNome()))
                .andExpect(jsonPath("$.tipo").value(produtoDTO.getTipo().toString()))
                .andExpect(jsonPath("$.precoUnitario").value(produtoDTO.getPrecoUnitario().toString()));
    }

    @Test
    @DisplayName("Deve retornar código status not found")
    public void deveRetornarCodigoStatusNotFound() throws Exception {

        UUID id = UUID.randomUUID();

        when(produtoService.buscarPorId(id)).thenThrow(RecursoNaoEncontradoException.class);

        mockMvc.perform(get("/api/v1/produtos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar page com lista de produtos")
    public void deveRetornarPageListaProdutos() throws Exception {

        UUID idProduto1 = UUID.randomUUID();
        UUID idProduto2 = UUID.randomUUID();

        var produtoDTO1 = getObterProdutoDTO(getProdutoEntity(idProduto1));

        var produtoDTO2 = getObterProdutoDTO(getProdutoEntity(idProduto2));

        var produtos = List.of(produtoDTO1, produtoDTO2);
        var page = new PageImpl<>(produtos);

        when(produtoService.listar(0, 10, "nome", "asc")).thenReturn(page);

        mockMvc.perform(get("/api/v1/produtos")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "nome")
                        .param("direction", "asc"))
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

        UUID id = UUID.randomUUID();
        var produtoEntity = getProdutoEntity(id);
        var criarProdutoDTO = getCriarProdutoDTO(produtoEntity);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

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

        UUID id = UUID.randomUUID();
        var produtoEntity = getProdutoEntity(id);
        var alterarProdutoDTO = getAlterarProdutoDTO(produtoEntity);
        var obterProdutoDTO = getObterProdutoDTO(produtoEntity);

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

        UUID id = UUID.randomUUID();
        var produtoDTO = getObterProdutoDTO(getProdutoEntity(id));

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
    public void deveRetornarQuantidadePrecoMedioPorTipo() throws Exception {

        var precoMedioDTO = getPrecoMedioProdutoDTO();

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