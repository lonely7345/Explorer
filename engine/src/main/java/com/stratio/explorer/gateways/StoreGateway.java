package com.stratio.explorer.gateways;

public interface StoreGateway<T> {

    T execute(String command);


}
