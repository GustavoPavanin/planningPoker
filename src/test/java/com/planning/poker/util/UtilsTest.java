package com.planning.poker.util;

import org.junit.jupiter.api.Test;

import static com.planning.poker.util.Utils.isNotNull;
import static com.planning.poker.util.Utils.isNull;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void deve_formatar_double_para_duas_casas(){
        Double valor = 10.123456789;
        Double valorFormatado = Utils.formatDouble(valor);
        assertEquals(valorFormatado, 10.12);
    }

    @Test
    public void deve_verificar_se_null(){
        Integer umNumero = 1;
        String umString = "conteúdo";
        assertFalse(isNull(umNumero));
        assertTrue(isNotNull(umString));
        assertTrue(isNull("null"));
    }
}
