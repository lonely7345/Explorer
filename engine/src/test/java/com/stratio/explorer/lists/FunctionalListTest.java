package com.stratio.explorer.lists;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.stratio.explorer.doubles.TransformFunctionDouble;
import com.stratio.explorer.doubles.TypeTestOneDouble;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by afidalgo on 14/10/15.
 */
public class FunctionalListTest {



    private FunctionalList <String,TypeTestOneDouble>functionalList;
    private List<String> entryList;


    @Before
    public void setUp(){
        entryList = new ArrayList<>();
        functionalList = new FunctionalList<String,TypeTestOneDouble>(entryList);
    }


    @Test
    public void whenCallMapFunctionTransformImputTypeInOutputType(){
        String first ="first";
        entryList.add(first);
        List<TypeTestOneDouble> resultList = functionalList.map(new TransformFunctionDouble());
        assertThat(resultList.size(),is(1));
        assertThat(resultList.get(0).entry(),is(first));
    }
}
