package com.api.repository;

import com.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByRazaoSocialContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
            String razaoSocial, String documento, Pageable pageable);
}
