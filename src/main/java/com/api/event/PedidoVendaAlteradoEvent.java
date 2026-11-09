package com.api.event;

import java.time.Instant;

public record PedidoVendaAlteradoEvent(Long pedidoId, Operacao operacao, Instant ocorridoEm) {
    public enum Operacao {
        CRIADO, ATUALIZADO, EXCLUIDO
    }
}
