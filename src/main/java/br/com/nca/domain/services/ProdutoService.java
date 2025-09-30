package br.com.nca.domain.services;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterPrecoMedioProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProdutoService {

	ObterProdutoDTO buscarPorId(UUID id);

	ObterProdutoDTO cadastrar(CriarProdutoDTO criarProdutoDTO);

	ObterProdutoDTO alterar(AlterarProdutoDTO alterarProdutoDTO);

	ObterProdutoDTO desativar(UUID id);

	List<ObterPrecoMedioProdutoDTO> obterPrecoMedioPorTipo();

    Page<ObterProdutoDTO> listar(int page, int size, String sortBy, String direction);
}