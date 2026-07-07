package com.example.productservice.service;

import com.example.productservice.dto.PendingMessageInfo;

public interface StreamMonitoringService {

    PendingMessageInfo getPendingMessages();

}