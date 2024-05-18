package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Conta;

import java.util.HashMap;
import java.util.Map;

public class ContaGatewayDBFake implements ContaGateway {
    public static Map<String, Conta> db = new HashMap<>();

    @Override
    public Conta buscarPorCpf(String cpf) {
        return db.get(cpf);
    }

    @Override
    public Conta salvar(Conta conta) {
        db.put(conta.getCpf(), conta);
        return db.get(conta.getCpf());
    }
}
