package com.api.repository;

import com.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNomeContainingIgnoreCaseOrTipoContainingIgnoreCase(
            String nome, String tipo, Pageable pageable);
}
