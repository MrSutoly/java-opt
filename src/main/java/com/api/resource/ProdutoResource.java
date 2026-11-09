package com.api.resource;

import com.api.model.Produto;
import com.api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoResource {
    private final ProdutoService service;

    public ProdutoResource(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Produto> listar(@RequestParam(required = false) String filtro,
                                @PageableDefault(size = 20) Pageable pageable) {
        return service.listar(filtro, pageable);
    }

    @GetMapping("/{codigo}")
    public Produto buscar(@PathVariable Long codigo) {
        return service.buscar(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@Valid @RequestBody Produto produto) {
        return service.salvar(produto);
    }

    @PutMapping("/{codigo}")
    public Produto atualizar(@PathVariable Long codigo, @Valid @RequestBody Produto produto) {
        return service.atualizar(codigo, produto);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long codigo) {
        service.excluir(codigo);
    }
}
