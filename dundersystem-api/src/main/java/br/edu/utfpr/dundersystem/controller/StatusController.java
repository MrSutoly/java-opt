package br.edu.utfpr.dundersystem.controller;

import br.edu.utfpr.dundersystem.repository.ClienteRepository;
import br.edu.utfpr.dundersystem.repository.FuncionarioRepository;
import br.edu.utfpr.dundersystem.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/** Endpoint simples para validar a conexão com o MySQL e as migrations do Flyway. */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final FuncionarioRepository funcionarios;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public StatusController(FuncionarioRepository funcionarios,
                            ClienteRepository clientes,
                            ProdutoRepository produtos) {
        this.funcionarios = funcionarios;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @GetMapping
    public Map<String, Object> status() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("aplicacao", "DunderSystem API");
        body.put("status", "OK");
        body.put("funcionarios", funcionarios.count());
        body.put("clientes", clientes.count());
        body.put("produtos", produtos.count());
        return body;
    }
}
