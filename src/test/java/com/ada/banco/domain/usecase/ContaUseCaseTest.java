package com.ada.banco.domain.usecase;

import com.ada.banco.domain.exception.ContaJaExisteException;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.EmailGateway;
import com.ada.banco.domain.model.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ContaUseCaseTest {

    @Mock
    private ContaGateway contaGateway;

    @Mock
    private EmailGateway emailGateway;

    @InjectMocks
    ContaUseCase contaUseCase;

    @Test
    public void deveCriarContaCorretamente() throws Exception {
        Conta conta = new Conta(1L, 1L, 1L, BigDecimal.ZERO, "Joao", "11111111111");

        Mockito.when(contaGateway.buscarPorCpf(conta.getCpf())).thenReturn(null);
        Mockito.when(contaGateway.salvar(conta)).thenReturn(conta);

        Assertions.assertEquals(conta, contaUseCase.criar(conta));

        Mockito.verify(contaGateway, Mockito.times(1)).salvar(conta);
        Mockito.verify(emailGateway, Mockito.times(1)).send(conta.getCpf());
    }

    @Test
    public void deveLancarExcecaoCasoContaExista() {
        Conta conta2 = new Conta(1L, 1L, 1L, BigDecimal.ZERO, "Maria", "2222222222");

        Mockito.when(contaGateway.buscarPorCpf(conta2.getCpf())).thenReturn(conta2);

        Throwable exception = Assertions.assertThrows(ContaJaExisteException.class, () -> {
            contaUseCase.criar(conta2);
        });

        Assertions.assertEquals("A conta ja existe", exception.getMessage());

        Mockito.verify(contaGateway, Mockito.times(0)).salvar(conta2);
        Mockito.verify(emailGateway, Mockito.times(0)).send(conta2.getCpf());
    }
}