package com.example.swii_proyectouber;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidaCedulaTest {

    @Test
    public void valida() {
        DriverRegisterActivity driv = new DriverRegisterActivity();
        Assert.assertEquals(true, driv.valida("1713062584"));
        Assert.assertEquals(false, driv.valida("1713062583"));
        Assert.assertEquals(false, driv.valida("00000"));
    }
}