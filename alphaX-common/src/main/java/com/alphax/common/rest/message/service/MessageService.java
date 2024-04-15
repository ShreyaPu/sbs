package com.alphax.common.rest.message.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

import com.alphax.common.rest.message.Message;

/**
 * Service for creating ApiMessage by string key and list of parameters.
 * @author A106744104
 */
@Service
public interface MessageService {
	

	 /**
     * Create {@link Message} that contains list of
     * {@link Message} with same key and provided
     * parameters.
     *
     * @param key        Key of message in messages.yml file.
     * @param parameters A list of parameters that will be used for formatting.
     *
     * @return {@link Message} for key
     */
	Message createApiMessage(String key, Object... parameters);

    /**
     * Create {@link Message} that contains key and provided
     * parameters.
     *
     * @param locale     The locale that is used to retrieve the localized message.
     *                   If it is null or message key is not found then the fallback
     *                   is to use the US English message.
     * @param key        Key of message in messages.yml file.
     * @param parameters A list of parameters that will be used for formatting.
     * @return {@link Message} for key
     */
    Message createApiMessage(Locale locale, String key, Object... parameters);

    
    /**
     * Loads messages to the context from the provided message file path.
     *
     * @param messagesFilePath Path of the message file resource.
     */
    void loadMessages(String messagesFilePath);
    
    
    /**
     * Loads localized messages from the provided resource bundle.
     *
     * @param baseName The base name of the resource bundle, a fully qualified class
     *                 name.
     */
    void addResourceBundleBaseName(String baseName);
    
    
    /**
     * Returns the message in the format that can be printed to console as a single
     * line or displayed to the user.
     *
     * @param key        Message key to be retrieved.
     * @param parameters Positional parameters require by the message.
     * @return Readable text for the given message key.
     */
    String getReadableMessage(String key, Object... parameters);


    /**
     * @return Returns the value of the default message source attribute that is
     *         added to every error message. It is a string that identifies the
     *         source service. For example: myhost:8080:serviceid
     */
    String getDefaultMessageSource();

    /**
     * @param defaultMessageSource The value of the default message source attribute
     *                             that is added to every error message. It is a
     *                             string that identifies the source service. For
     *                             example: myhost:8080:serviceid
     */
    void setDefaultMessageSource(String defaultMessageSource);

}
