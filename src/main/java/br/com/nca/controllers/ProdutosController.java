package br.com.nca.controllers;

import br.com.nca.domain.dtos.AlterarProdutoDTO;
import br.com.nca.domain.dtos.CriarProdutoDTO;
import br.com.nca.domain.dtos.ObterPrecoMedioProdutoDTO;
import br.com.nca.domain.dtos.ObterProdutoDTO;
import br.com.nca.domain.services.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<Page<ObterProdutoDTO>> listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        var produtos = produtoService.listar(page, size, sortBy, direction);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<ObterProdutoDTO> cadastrar(@RequestBody @Valid CriarProdutoDTO criarProdutoDTO) {
        var obterProdutoDTO = produtoService.cadastrar(criarProdutoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(obterProdutoDTO);
    }

    @PatchMapping
    public ResponseEntity<ObterProdutoDTO> alterar(@RequestBody AlterarProdutoDTO alterarProdutoDTO) {
        var obterProdutoDTO = produtoService.alterar(alterarProdutoDTO);
        return ResponseEntity.ok(obterProdutoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ObterProdutoDTO> excluir(@PathVariable UUID id) {
        var obterProdutoDTO = produtoService.desativar(id);
        return ResponseEntity.ok(obterProdutoDTO);
    }

    @GetMapping("/preco-medio")
    public ResponseEntity<List<ObterPrecoMedioProdutoDTO>> getPrecoMedioProduto() {
        var listaPrecoMedioTipoDTO = produtoService.obterPrecoMedioPorTipo();
        return ResponseEntity.ok(listaPrecoMedioTipoDTO);
    }
}