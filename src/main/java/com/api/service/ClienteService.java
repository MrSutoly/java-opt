package com.api.service;

import com.api.model.Cliente;
import com.api.model.Funcionario;
import com.api.dto.ClienteResponse;
import com.api.repository.ClienteRepository;
import com.api.repository.FuncionarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    private final FuncionarioRepository funcionarioRepository;

    public ClienteService(ClienteRepository repository, FuncionarioRepository funcionarioRepository) {
        this.repository = repository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(String filtro, Pageable pageable) {
        if (filtro == null || filtro.isBlank()) {
            return repository.findAll(pageable).map(ClienteResponse::de);
        }
        return repository.findByRazaoSocialContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
                filtro.trim(), filtro.trim(), pageable).map(ClienteResponse::de);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long codigo) {
        return ClienteResponse.de(buscarEntidade(codigo));
    }

    @Transactional
    public ClienteResponse salvar(Cliente cliente) {
        cliente.setId(null);
        cliente.setVendedor(vendedorAtivo(cliente.getVendedor()));
        return ClienteResponse.de(repository.save(cliente));
    }

    @Transactional
    public ClienteResponse atualizar(Long codigo, Cliente fonte) {
        Cliente entidade = buscarEntidade(codigo);
        Funcionario vendedor = vendedorAtivo(fonte.getVendedor());
        BeanUtils.copyProperties(fonte, entidade, "id", "vendedor", "dataCriacao");
        entidade.setVendedor(vendedor);
        return ClienteResponse.de(repository.save(entidade));
    }

    @Transactional
    public void excluir(Long codigo) {
        repository.delete(buscarEntidade(codigo));
    }

    private Cliente buscarEntidade(Long codigo) {
        return repository.findById(codigo).orElseThrow(
                () -> new EmptyResultDataAccessException("Cliente não encontrado", 1));
    }

    private Funcionario vendedorAtivo(Funcionario referencia) {
        if (referencia == null || referencia.getId() == null) {
            throw new IllegalArgumentException("Informe o código do vendedor.");
        }
        Funcionario vendedor = funcionarioRepository.findById(referencia.getId()).orElseThrow(
                () -> new EmptyResultDataAccessException("Vendedor não encontrado", 1));
        if (!Boolean.TRUE.equals(vendedor.getAtivo())) {
            throw new IllegalStateException("Não é possível vincular um vendedor inativo.");
        }
        return vendedor;
    }
}
