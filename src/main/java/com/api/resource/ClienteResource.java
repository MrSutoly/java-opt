package com.api.resource;

import com.api.model.Cliente;
import com.api.dto.ClienteResponse;
import com.api.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteResource {
    private final ClienteService service;

    public ClienteResource(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ClienteResponse> listar(@RequestParam(required = false) String filtro,
                                        @PageableDefault(size = 20) Pageable pageable) {
        return service.listar(filtro, pageable);
    }

    @GetMapping("/{codigo}")
    public ClienteResponse buscar(@PathVariable Long codigo) {
        return service.buscar(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    @PutMapping("/{codigo}")
    public ClienteResponse atualizar(@PathVariable Long codigo, @Valid @RequestBody Cliente cliente) {
        return service.atualizar(codigo, cliente);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long codigo) {
        service.excluir(codigo);
    }
}
