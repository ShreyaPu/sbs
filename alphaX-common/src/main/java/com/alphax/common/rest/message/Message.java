package com.alphax.common.rest.message;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * User facing messages that can be provided with API responses.
 *
 * We should include as much useful data as possible and keep in mind different
 * users of the message structure.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface Message {
    /**
     * The severity of a problem. This field is required.
     */
    MessageType getMessageType();

    /**
     * Typical mainframe message number.
     *
     * Example: "1100"
     */
    String getMessageNumber();

    /**
     * Readable message in US English. This field is required. It should be a
     * sentence starting with a capital letter and ending with a full stop (.).
     */
    String getMessageContent();


    /**
     * Optional unique key describing the reason of the error. It should be a dot
     * delimited string "com.ca.service[.subservice].detail". The purpose of this
     * field is to enable UI to show a meaningful and localized error message.
     */
    String getMessageKey();
    
    
    /**
     * Optional message locale describing the message language.
     */
    String getMessageLocale();


    /**
     * Returns message in the format that can be printed to console as a single line
     * or displayed to the user.
     **/
    String toReadableText();
}
