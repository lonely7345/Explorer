package com.stratio.explorer.notebook.utils;


public class KeyValue {

    private String key,value;

    public KeyValue(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String key(){
       return key;
    }

    public String value(){
       return value;
    }

    public String toStringSeparateBysimbol(String simbol){
       return key + simbol + value;
    }
}
