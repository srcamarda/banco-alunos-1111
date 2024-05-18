package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.ContaGatewayDBFake;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.EmailGatewayDummy;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CriarNovaContaTest {
    // Ao criar uma conta ela deve ser salva no banco de dados
    // Deve lancar uma exception caso a conta ja exista

    @Test
    public void deveLancarExceptionCasoAContaJaExista() {
        // Dado
        ContaGateway contaGateway = new ContaGatewayDBFake();
        EmailGateway emailGateway = new EmailGatewayDummy();
        ContaUseCase contaUseCase = new ContaUseCase(contaGateway, emailGateway);

        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");
        contaGateway.salvar(conta);

        // When
        Throwable throwable = Assertions.assertThrows(ContaJaExisteException.class, () -> contaUseCase.criar(conta));

        // Then
        Assertions.assertEquals("A conta ja existe", throwable.getMessage());
    }

    @Test
    public void deveSalvarAContaComSucesso() throws Exception {
        // Dado
        ContaGateway contaGateway = new ContaGatewayDBFake();
        EmailGateway emailGateway = new EmailGatewayDummy();
        ContaUseCase contaUseCase = new ContaUseCase(contaGateway, emailGateway);

        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");

        // Quando
        contaUseCase.criar(conta);

        // Entao
        Conta contaEncontrada = contaGateway.buscarPorCpf(conta.getCpf());
        Assertions.assertEquals(conta, contaEncontrada);
    }


}
