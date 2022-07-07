package com.alibaba.json.bvt;

import java.io.IOException;
import java.util.*;

import clojure.lang.Obj;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import org.json.JSONObject;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class MapTest2 extends TestCase {

    private List<Object> key;
    private List<Object> value;
    private String stringToConvert;
    private HashMap<Object,Object> mapExpected;
    private boolean expectedNUll;
    private boolean expectedJsonException;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Arrays.asList(1,"3",'5'),Arrays.asList("2",4,6)},
                { Arrays.asList(1,4,"6"),Arrays.asList("2","3",'5')},
                { Arrays.asList(1,4,6),Arrays.asList(2,3,5)},
                { Arrays.asList("1","4","6"),Arrays.asList("2","3","5")},
                { Arrays.asList("1","4","6"),Arrays.asList(2,3,5)},
                { Arrays.asList(null,"4","6"),Arrays.asList(2,3,5)},
                { Arrays.asList(null,"4","6"),Arrays.asList(null,3,5)},
                {Arrays.asList(), Arrays.asList()},
                { null,null},

        });
    }

    private void configure() {

        if(key==null || value==null)
            return;

        if(key.size() ==0 || value.size()==0){
            stringToConvert="";
            return;
        }


        mapExpected = new HashMap<>();

        stringToConvert="{";
        for(int i=0; i<key.size();i++){

            mapExpected.put(key.get(i) ,value.get(i));

            if(key.get(i) instanceof Integer)
                stringToConvert=stringToConvert+key.get(i);

            if(key.get(i) instanceof String)
                stringToConvert=stringToConvert+"\""+key.get(i)+"\"";

            if(key.get(i) instanceof Character)
                stringToConvert=stringToConvert+"'"+key.get(i)+"'";

            stringToConvert=stringToConvert+":";

            if(value.get(i) instanceof Integer)
                stringToConvert=stringToConvert+value.get(i);

            if(value.get(i) instanceof String)
                stringToConvert=stringToConvert+"\""+value.get(i)+"\"";

            if(value.get(i) instanceof Character)
                stringToConvert=stringToConvert+"'"+value.get(i)+"'";

            stringToConvert=stringToConvert+",";

        }

        stringToConvert= stringToConvert.substring(0,stringToConvert.length()-1);
        stringToConvert=stringToConvert+"}";

    }

    public void getOracle(){
        if(stringToConvert==null || stringToConvert.length()==0)
            expectedNUll=true;
        if(key!=null && value!=null && (key.contains(null) || value.contains(null)))
            expectedJsonException = true;
    }

    public MapTest2(List<Object> key,List<Object> value ) {
        this.key=key;
        this.value=value;
        configure();
        getOracle();
    }


    @Test
    public void test_map ()  {


        try {
            Map<Object, Object> map = JSON.parseObject(stringToConvert, new TypeReference<Map<Object, Object>>() {});

            if(expectedNUll)
                assertNull(map);
            else{
                Set<Object> keySet = mapExpected.keySet();
                Object key;
                Object valueExpected;

                for(Object keyPartial: keySet) {
                    valueExpected = mapExpected.get(keyPartial);

                    if (keyPartial instanceof Character)
                        key = String.valueOf(keyPartial);
                    else
                        key = keyPartial;

                    if (valueExpected instanceof Character)
                        valueExpected = String.valueOf(valueExpected);


                    Assert.assertEquals(valueExpected, map.get(key));
                }
            }
        }catch(JSONException e){
            assertTrue(expectedJsonException);
        }


    }


}

