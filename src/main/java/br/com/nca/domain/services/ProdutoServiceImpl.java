package br.com.nca.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterPrecoMedioProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.entities.Produto;
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
		var produto = modelMapper.map(criarProdutoDTO,Produto.class);
		produto.setAtivo(true);
		var produtoCadastrado = produtoRepository.save(produto);
		return modelMapper.map(produtoCadastrado, ObterProdutoDTO.class);
	}

	@Override
	public ObterProdutoDTO alterar(AlterarProdutoDTO alterarProdutoDTO) {

		var produto = produtoRepository.findByIdAndAtivoTrue(alterarProdutoDTO.getId());

		if (produto.isEmpty()) {
			throw new RecursoNaoEncontradoException();
		}

		alterarCampos(alterarProdutoDTO, produto.get());

		var produtoAlterardo = produtoRepository.save(produto.get());
	   return modelMapper.map(produtoAlterardo, ObterProdutoDTO.class);
	}

	@Override
	public ObterProdutoDTO desativar(UUID id) {

		var produto = produtoRepository.findByIdAndAtivoTrue(id)
				.orElseThrow(RecursoNaoEncontradoException::new);

		produto.setAtivo(false);
		produtoRepository.save(produto);

		return modelMapper.map(produto, ObterProdutoDTO.class);
	}

	@Override
	public List<ObterPrecoMedioProdutoDTO> obterPrecoMedioPorTipo() {
		// TODO Auto-generated method stub
		return null;
	}

	private void alterarCampos(AlterarProdutoDTO dto, Produto produto) {
		Optional.ofNullable(dto.getNome()).ifPresent(produto::setNome);
		Optional.ofNullable(dto.getTipo()).ifPresent(produto::setTipo);
		Optional.ofNullable(dto.getPrecoUnitario()).ifPresent(produto::setPrecoUnitario);
	}
}