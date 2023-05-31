package com.example.airhockey.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.airhockey.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventHandlerRegistry {
    private final Map<String, EventHandler> handlers = new HashMap<>();

    @Autowired
    public EventHandlerRegistry(List<EventHandler> eventHandlers) {
        for (EventHandler handler : eventHandlers) {
            handlers.put(handler.getEventType().getName(), handler);
        }
    }
    public EventHandler getHandler(String commandType) {
        return handlers.get(commandType);
    }

    public Set<String> getHandlerTypes() {
        return handlers.keySet();
    }
}
