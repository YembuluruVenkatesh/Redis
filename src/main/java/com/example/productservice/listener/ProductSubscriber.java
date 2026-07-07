package com.example.productservice.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductSubscriber {

    public void receiveMessage(String message) {

        log.info("ProductSubscriber  ---  Received Event : {}", message);

    }

}