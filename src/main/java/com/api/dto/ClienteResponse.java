package com.api.dto;

import com.api.model.Cliente;

import java.time.LocalDateTime;

public record ClienteResponse(Long id, String razaoSocial, String documento, String email,
                              String telefone, String endereco, String cidade, String estado,
                              Boolean ativo, Long vendedorId, String vendedor,
                              LocalDateTime dataCriacao) {
    public static ClienteResponse de(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getRazaoSocial(), cliente.getDocumento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getEndereco(), cliente.getCidade(),
                cliente.getEstado(), cliente.getAtivo(), cliente.getVendedor().getId(),
                cliente.getVendedor().getNome(), cliente.getDataCriacao());
    }
}
