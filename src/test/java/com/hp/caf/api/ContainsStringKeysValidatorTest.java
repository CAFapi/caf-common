package com.hp.caf.api;

import org.junit.Assert;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

public class ContainsStringKeysValidatorTest
{
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    public void testValidator()
    {
        TestClass test = new TestClass();
        Map<String, Integer> a = new HashMap<>();
        a.put("testKey", 1);
        test.setTestMap(a);
        Assert.assertTrue(validator.validate(test).isEmpty());
        a.clear();
        a.put("stuff", 2);
        Assert.assertFalse(validator.validate(test).isEmpty());
    }


    private class TestClass
    {
        @ContainsStringKeys(keys = {"testKey"})
        Map<String, Integer> testMap;


        public Map<String, Integer> getTestMap() {
            return testMap;
        }

        public void setTestMap(Map<String, Integer> testMap) {
            this.testMap = testMap;
        }
    }
}
