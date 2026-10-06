package br.edu.utfpr.dundersystem.repository;

import br.edu.utfpr.dundersystem.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
