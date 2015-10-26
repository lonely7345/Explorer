package com.stratio.explorer.lists;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.stratio.explorer.doubles.DoubleSearch;
import com.stratio.explorer.doubles.TransformFunctionDouble;
import com.stratio.explorer.doubles.TypeTestOneDouble;
import com.stratio.explorer.functions.SearcherFunction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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


    @Test
    public void whenCallSearchFunctionAndExist(){
        String searchedValue = "Any";
        entryList.add(searchedValue);
        entryList.add("Not Fou");
        SearcherFunction function = new DoubleSearch(searchedValue);
        List<String> resultList = functionalList.search(function);
        assertThat(resultList,is(Arrays.asList(searchedValue)));
    }


    @Test
    public void whenCallSearchFunctionAndNotExist(){
        String expectedValue = "Any";
        List<String> expect = new ArrayList<>();
        entryList.add("not found");
        entryList.add("Not Fou");
        SearcherFunction function = new DoubleSearch(expectedValue);
        List<String> resultList = functionalList.search(function);
        assertThat(resultList,is(expect));
    }
}
