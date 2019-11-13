package com.example.activite_threadui;

import java.util.EventListener;

public interface CommunicationEventListener extends EventListener {
    public boolean handleServerResponse(String response);

}
