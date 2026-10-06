package br.edu.utfpr.dundersystem.repository;

import br.edu.utfpr.dundersystem.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
