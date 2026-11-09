package com.api.resource;

import com.api.model.Funcionario;
import com.api.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioResource {
    private final FuncionarioService service;

    public FuncionarioResource(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Funcionario> listar(@RequestParam(required = false) String filtro,
                                    @PageableDefault(size = 20) Pageable pageable) {
        return service.listar(filtro, pageable);
    }

    @GetMapping("/{codigo}")
    public Funcionario buscar(@PathVariable Long codigo) {
        return service.buscar(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario criar(@Valid @RequestBody Funcionario funcionario) {
        return service.salvar(funcionario);
    }

    @PutMapping("/{codigo}")
    public Funcionario atualizar(@PathVariable Long codigo, @Valid @RequestBody Funcionario funcionario) {
        return service.atualizar(codigo, funcionario);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long codigo) {
        service.excluir(codigo);
    }
}
