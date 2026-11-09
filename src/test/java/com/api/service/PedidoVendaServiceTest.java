package com.api.service;

import com.api.model.Cliente;
import com.api.model.Funcionario;
import com.api.model.ItemPedido;
import com.api.model.PedidoVenda;
import com.api.model.Produto;
import com.api.repository.ClienteRepository;
import com.api.repository.FuncionarioRepository;
import com.api.repository.PedidoVendaRepository;
import com.api.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PedidoVendaServiceTest {
    private final PedidoVendaRepository pedidos = mock(PedidoVendaRepository.class);
    private final ClienteRepository clientes = mock(ClienteRepository.class);
    private final FuncionarioRepository funcionarios = mock(FuncionarioRepository.class);
    private final ProdutoRepository produtos = mock(ProdutoRepository.class);
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private final PedidoVendaService service =
            new PedidoVendaService(pedidos, clientes, funcionarios, produtos, eventPublisher);

    @Test
    void naoCriaPedidoParaClienteInativo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setAtivo(false);
        when(clientes.findById(1L)).thenReturn(Optional.of(cliente));

        PedidoVenda pedido = new PedidoVenda();
        pedido.setCliente(referenciaCliente(1L));

        assertThrows(IllegalStateException.class, () -> service.salvar(pedido));
    }

    @Test
    void calculaSubtotalEDescontoComPrecoVigenteDoProduto() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRazaoSocial("Cliente Teste");
        cliente.setAtivo(true);
        when(clientes.findById(1L)).thenReturn(Optional.of(cliente));

        Funcionario vendedor = new Funcionario();
        vendedor.setId(2L);
        vendedor.setNome("Vendedor Teste");
        vendedor.setAtivo(true);
        when(funcionarios.findById(2L)).thenReturn(Optional.of(vendedor));

        Produto produto = new Produto();
        produto.setId(3L);
        produto.setNome("Papel A4");
        produto.setPrecoUnitario(new BigDecimal("27.00"));
        produto.setAtivo(true);
        when(produtos.findById(3L)).thenReturn(Optional.of(produto));
        when(pedidos.save(any(PedidoVenda.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        PedidoVenda pedido = new PedidoVenda();
        pedido.setCliente(referenciaCliente(1L));
        pedido.setVendedor(referenciaFuncionario(2L));
        pedido.setDescontoPercentual(new BigDecimal("10"));
        ItemPedido item = new ItemPedido();
        item.setProduto(referenciaProduto(3L));
        item.setQuantidade(2);
        pedido.adicionarItem(item);

        var resposta = service.salvar(pedido);

        assertEquals(new BigDecimal("54.00"), resposta.valorBruto());
        assertEquals(new BigDecimal("48.60"), resposta.valorTotal());
        assertEquals(new BigDecimal("27.00"), resposta.itens().get(0).precoUnitario());
    }

    private Cliente referenciaCliente(Long codigo) {
        Cliente cliente = new Cliente();
        cliente.setId(codigo);
        return cliente;
    }

    private Funcionario referenciaFuncionario(Long codigo) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(codigo);
        return funcionario;
    }

    private Produto referenciaProduto(Long codigo) {
        Produto produto = new Produto();
        produto.setId(codigo);
        return produto;
    }
}
