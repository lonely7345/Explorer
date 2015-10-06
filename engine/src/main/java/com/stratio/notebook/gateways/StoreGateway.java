package com.stratio.notebook.gateways;

public interface StoreGateway<T> {

    T execute(String command);


}
