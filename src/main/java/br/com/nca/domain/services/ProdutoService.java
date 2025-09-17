package br.com.nca.domain.services;

import java.util.List;
import java.util.UUID;

import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;

public interface ProdutoService {

	ObterProdutoDTO buscarPorId(UUID id);

	List<ObterProdutoDTO> listar();

	ObterProdutoDTO cadastrar(CriarProdutoDTO criarProdutoDTO);
}