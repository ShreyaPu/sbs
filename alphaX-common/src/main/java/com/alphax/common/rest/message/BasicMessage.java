package com.alphax.common.rest.message;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Holds information about a {@link Message}. This class is immutable.
 * <p>
 * Example:
 *
 * <pre>
 * {
 *     &#64;code
 *     Message message = new BasicMessage(MessageType.ERROR, "MAS0001", "Message text.");
 * }
 * </pre>
 *
 */
@JsonPropertyOrder({ "messageType", "messageNumber", "messageContent", "messageKey", "messageLocale"})
public class BasicMessage implements Message {
    private final MessageType messageType;
    private final String messageNumber;
    private final String messageContent;
    private final String messageKey;
    private final String messageLocale;

    
    @JsonCreator
    public BasicMessage(@JsonProperty("messageType") MessageType messageType,
            @JsonProperty("messageNumber") String messageNumber, @JsonProperty("messageContent") String messageContent,
            @JsonProperty("messageKey") String messageKey, @JsonProperty("messageLocale") String messageLocale ) {
        this.messageType = messageType;
        this.messageNumber = messageNumber;
        this.messageContent = messageContent;
        this.messageKey = messageKey;
        this.messageLocale = messageLocale;
       
    }

    public BasicMessage(@JsonProperty("messageType") MessageType messageType,
            @JsonProperty("messageNumber") String messageNumber,
            @JsonProperty("messageContent") String messageContent) {
        this(messageType, messageNumber, messageContent, null, null);
    }

    public BasicMessage(@JsonProperty("messageKey") String messageKey,
            @JsonProperty("messageType") MessageType messageType, @JsonProperty("messageNumber") String messageNumber,
            @JsonProperty("messageContent") String messageContent,
            @JsonProperty("messageLocale") String messageLocale) {
        this(messageType, messageNumber, messageContent, messageKey, messageLocale);
    }


    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public String getMessageNumber() {
        return messageNumber;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
    
    @Override
	public String getMessageLocale() {
		return messageLocale;
	}


    @Override
    public String toReadableText() {
        return getMessageNumber() + " " + getMessageType().toChar() + " " + getMessageContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicMessage that = (BasicMessage) o;
        return messageType == that.messageType && Objects.equals(messageNumber, that.messageNumber)
                && Objects.equals(messageContent, that.messageContent)
                && Objects.equals(messageKey, that.messageKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, messageNumber, messageContent, messageKey);
    }

    @Override
    public String toString() {
        return "BasicMessage{" + "messageType=" + messageType + ", messageNumber='" + messageNumber + '\''
                + ", messageContent='" + messageContent + '\''
                + ", messageKey='" + messageKey + '\'' +  "}";
                
    }

}