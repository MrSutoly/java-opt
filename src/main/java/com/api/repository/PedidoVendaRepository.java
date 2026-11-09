package com.api.repository;

import com.api.model.PedidoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.api.model.enums.StatusPedido;

public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Long> {
    Page<PedidoVenda> findByStatus(StatusPedido status, Pageable pageable);
}
