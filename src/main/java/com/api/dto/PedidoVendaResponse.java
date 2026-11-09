package com.api.dto;

import com.api.model.PedidoVenda;
import com.api.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoVendaResponse(Long id, Long clienteId, String cliente, Long vendedorId,
                                 String vendedor, Long aprovadorId, StatusPedido status,
                                 BigDecimal descontoPercentual, BigDecimal valorBruto,
                                 BigDecimal valorTotal, String observacao,
                                 String justificativaDecisao, LocalDateTime dataCriacao,
                                 LocalDateTime dataDecisao, List<ItemResponse> itens) {
    public static PedidoVendaResponse de(PedidoVenda pedido) {
        List<ItemResponse> itens = pedido.getItens().stream()
                .map(item -> new ItemResponse(item.getId(), item.getProduto().getId(),
                        item.getProduto().getNome(), item.getQuantidade(),
                        item.getPrecoUnitario(), item.getSubtotal()))
                .toList();
        return new PedidoVendaResponse(pedido.getId(), pedido.getCliente().getId(),
                pedido.getCliente().getRazaoSocial(), pedido.getVendedor().getId(),
                pedido.getVendedor().getNome(),
                pedido.getAprovador() == null ? null : pedido.getAprovador().getId(),
                pedido.getStatus(), pedido.getDescontoPercentual(), pedido.getValorBruto(),
                pedido.getValorTotal(), pedido.getObservacao(), pedido.getJustificativaDecisao(),
                pedido.getDataCriacao(), pedido.getDataDecisao(), itens);
    }

    public record ItemResponse(Long id, Long produtoId, String produto, Integer quantidade,
                               BigDecimal precoUnitario, BigDecimal subtotal) {
    }
}
