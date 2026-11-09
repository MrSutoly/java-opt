package com.api.service;

import com.api.model.Funcionario;
import com.api.repository.FuncionarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class FuncionarioService {
    private final FuncionarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<Funcionario> listar(String filtro, Pageable pageable) {
        if (filtro == null || filtro.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
                filtro.trim(), filtro.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Funcionario buscar(Long codigo) {
        return repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Funcionário não encontrado", 1));
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        if (!StringUtils.hasText(funcionario.getSenha())) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        funcionario.setId(null);
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return repository.save(funcionario);
    }

    @Transactional
    public Funcionario atualizar(Long codigo, Funcionario fonte) {
        Funcionario destino = buscar(codigo);
        if (fonte.getSenha() != null && !StringUtils.hasText(fonte.getSenha())) {
            throw new IllegalArgumentException("A senha não pode ficar em branco.");
        }
        if (fonte.getSenha() == null) {
            BeanUtils.copyProperties(fonte, destino, "id", "senha", "dataCriacao");
        } else {
            fonte.setSenha(passwordEncoder.encode(fonte.getSenha()));
            BeanUtils.copyProperties(fonte, destino, "id", "dataCriacao");
        }
        return repository.save(destino);
    }

    @Transactional
    public void excluir(Long codigo) {
        repository.delete(buscar(codigo));
    }
}
