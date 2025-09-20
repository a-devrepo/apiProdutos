package br.com.nca.domain.services;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterPrecoMedioProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.exceptions.RecursoNaoEncontradoException;
import br.com.nca.domain.repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

	private final ProdutoRepository produtoRepository;
	
	private final ModelMapper modelMapper;
	
	
	@Override
	public ObterProdutoDTO buscarPorId(UUID id) {
		var produto = produtoRepository.findByIdAndAtivoTrue(id);
		
		if (produto.isEmpty()) {
			throw new RecursoNaoEncontradoException();
		}
		
		return modelMapper.map(produto.get(), ObterProdutoDTO.class);
	}

	@Override
	public List<ObterProdutoDTO> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObterProdutoDTO cadastrar(CriarProdutoDTO criarProdutoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObterProdutoDTO alterar(AlterarProdutoDTO alterarProdutoDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObterProdutoDTO desativar(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObterPrecoMedioProdutoDTO> obterPrecoMedioPorTipo() {
		// TODO Auto-generated method stub
		return null;
	}
}