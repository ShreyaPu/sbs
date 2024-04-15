package com.alphax.common.rest.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alphax.common.rest.message.ErrorMessage;
import com.alphax.common.rest.message.ErrorMessages;

import lombok.extern.slf4j.Slf4j;

/**
 * Error message storage class
 */
@Slf4j
public class MessageStorage {
    private final Map<String, ErrorMessage> keyMap = new HashMap<>();
    private final Map<String, ErrorMessage> numberMap = new HashMap<>();

    /**
     * Retrieve error message from the storage by message key
     *
     * @param key Message Key
     * @return Error message
     */
    public ErrorMessage getErrorMessage(String key) {
        return keyMap.get(key);
    }

    /**
     * Method for adding messages to storage
     *
     * @param messages Error message
     */
    public void addMessages(ErrorMessages messages) {
        Map<String, ErrorMessage> currentKeyMap = new HashMap<>();
        for (ErrorMessage message : messages.getMessages()) {
            if (!currentKeyMap.containsKey(message.getKey())) {
                if (!numberMap.containsKey(message.getNumber())) {
                    currentKeyMap.put(message.getKey(), message);
                    keyMap.put(message.getKey(), message);
                    numberMap.put(message.getNumber(), message);
                } else {
                    log.debug(String.format("Message with number '%s' already exists", message.getNumber()));
                }
            } else {
            	 log.debug(String.format("Message with key '%s' already exists", message.getKey()));
            }
        }
    }
}
