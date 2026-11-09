package com.api.service;

import com.api.model.Produto;
import com.api.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Produto> listar(String filtro, Pageable pageable) {
        if (filtro == null || filtro.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNomeContainingIgnoreCaseOrTipoContainingIgnoreCase(
                filtro.trim(), filtro.trim(), pageable);
    }

    @Transactional(readOnly = true)
    public Produto buscar(Long codigo) {
        return repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Produto não encontrado", 1));
    }

    @Transactional
    public Produto salvar(Produto produto) {
        produto.setId(null);
        return repository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long codigo, Produto fonte) {
        Produto destino = buscar(codigo);
        BeanUtils.copyProperties(fonte, destino, "id", "dataCriacao");
        return repository.save(destino);
    }

    @Transactional
    public void excluir(Long codigo) {
        repository.delete(buscar(codigo));
    }
}
