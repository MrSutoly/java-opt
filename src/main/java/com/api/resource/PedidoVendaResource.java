package com.api.resource;

import com.api.dto.PedidoVendaResponse;
import com.api.model.PedidoVenda;
import com.api.model.enums.StatusPedido;
import com.api.service.PedidoVendaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoVendaResource {
    private final PedidoVendaService service;

    public PedidoVendaResource(PedidoVendaService service) {
        this.service = service;
    }

    @GetMapping
    public Page<PedidoVendaResponse> listar(@RequestParam(required = false) StatusPedido status,
                                            @PageableDefault(size = 20) Pageable pageable) {
        return service.listar(status, pageable);
    }

    @GetMapping("/{codigo}")
    public PedidoVendaResponse buscar(@PathVariable Long codigo) {
        return service.buscar(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoVendaResponse criar(@Valid @RequestBody PedidoVenda pedido) {
        return service.salvar(pedido);
    }

    @PutMapping("/{codigo}")
    public PedidoVendaResponse atualizar(@PathVariable Long codigo, @Valid @RequestBody PedidoVenda pedido) {
        return service.atualizar(codigo, pedido);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long codigo) {
        service.excluir(codigo);
    }
}
