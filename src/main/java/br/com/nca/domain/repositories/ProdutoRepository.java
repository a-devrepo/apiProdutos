package br.com.nca.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.nca.domain.entities.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

	Optional<Produto>findByIdAndAtivoTrue(UUID id);
	Page<Produto> findByAtivoTrue(Pageable pageable);
}