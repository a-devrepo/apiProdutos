package br.com.nca.domain.services;

import java.util.UUID;

import br.com.nca.domain.dtos.ObterProdutoDTO;

public interface ProdutoService {

	ObterProdutoDTO buscarPorId(UUID id);
}
