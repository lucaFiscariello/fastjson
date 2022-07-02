package com.alibaba.json.bvt.serializer;

import org.json.JSONObject;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ParserConfigTest extends TestCase {


    private static ParserConfig config;
    private String stringTest;
    private int expected;
    private boolean expectedException;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "{\"value\":123}",},
                { "{\"value\":"+null+"}"},
                { "{\"value\":str}"},
                { "{\"value\":0.0}"},
                { "{\"value\":0L}"},
                { "{\"[value]\":0L}"},
                { "[\"value\":0L]"},
                { ""},
                { null},
        });
    }

    private void configure(String string) {
        config = new ParserConfig(Thread.currentThread().getContextClassLoader());
        stringTest=string;
    }

    public ParserConfigTest(String string) throws Exception {
        configure(string);
        getOracle();
    }

    @Test
    public void test0() throws IOException {
        config.getDeserializers();
    }

    @Test
    public void test_1() throws Exception {
        try{
            Model model = JSON.parseObject(stringTest, Model.class, config);
            Assert.assertEquals(expected, model.value);
        }catch (Exception e){
            assertTrue(expectedException);
        }

    }

    private void getOracle() {
        try{
            JSONObject jsonObject = new JSONObject(stringTest);
            expected= jsonObject.getInt("value");
        }catch (Exception e){
            expectedException=true;
        }

    }
    public static class Model {
        public int value;
    }
}


