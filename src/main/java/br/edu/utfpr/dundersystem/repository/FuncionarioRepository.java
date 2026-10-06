package br.edu.utfpr.dundersystem.repository;

import br.edu.utfpr.dundersystem.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
