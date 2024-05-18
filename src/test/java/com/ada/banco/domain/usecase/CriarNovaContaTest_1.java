package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.ContaGatewayDBFake;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.gateway.EmailGatewayDummy;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class CriarNovaContaTest_1 {
    // Ao criar uma conta ela deve ser salva no banco de dados
    // Deve lancar uma exception caso a conta ja exista

    private ContaGateway contaGateway;
    private EmailGateway emailGateway;
    private ContaUseCase contaUseCase;

    @BeforeEach
    public void beforeEach() {
        contaGateway = Mockito.mock(ContaGateway.class);
        emailGateway = Mockito.mock(EmailGateway.class);
        contaUseCase = new ContaUseCase(contaGateway, emailGateway);
    }

    @Test
    public void deveLancarExceptionCasoAContaJaExista() {
        // Mockito -> Mocks
        // Dado
        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");
        Mockito.when(contaGateway.buscarPorCpf("123456789")).thenReturn(conta);

        // When
        Throwable throwable = Assertions.assertThrows(ContaJaExisteException.class, () -> contaUseCase.criar(conta));

        // Then
        Assertions.assertEquals("A conta ja existe", throwable.getMessage());
        Mockito.verify(contaGateway, Mockito.never()).salvar(conta);
        Mockito.verify(emailGateway, Mockito.never()).send("123456789");
    }

    @Test
    public void deveSalvarAContaComSucesso() throws Exception {
        // Dado
        Conta conta =
                new Conta(1L, 2L, 3L, BigDecimal.ZERO, "Pedro", "123456789");
        Mockito.when(contaGateway.salvar(conta)).thenReturn(conta);


        // Quando// Entao
        contaUseCase.criar(conta);

        Mockito.verify(contaGateway, Mockito.times(1)).salvar(conta);
        Mockito.verify(emailGateway, Mockito.times(1)).send("123456789");
    }


}
