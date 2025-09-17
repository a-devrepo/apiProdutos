package br.com.nca.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.services.ProdutoService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutosController {
	
	private final ProdutoService produtoService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ObterProdutoDTO> getProdutoById(@PathVariable UUID id) {
		var produto = produtoService.buscarPorId(id);
		return ResponseEntity.ok(produto);
	}
}
