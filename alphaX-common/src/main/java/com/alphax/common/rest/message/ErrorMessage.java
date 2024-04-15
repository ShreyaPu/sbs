package com.alphax.common.rest.message;


import com.alphax.common.rest.message.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String key;
    private String number;
    private MessageType type;
    private String text;

}
