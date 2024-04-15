package com.alphax.common.rest.message.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.alphax.common.rest.message.BasicMessage;
import com.alphax.common.rest.message.ErrorMessage;
import com.alphax.common.rest.message.ErrorMessages;
import com.alphax.common.rest.message.Message;
import com.alphax.common.rest.message.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of {@link MessageService} that loads messages from YAML
 * files.
 * 
 * @author A106744104
 *
 */
@Slf4j
public class MessageServiceImpl implements MessageService {
	
	private static final String DEFAULT_MESSAGES_BASENAME = "messages";
    private static final String YAML_EXTENSION = ".yml";
    private static final String INVALID_MESSAGE_TEXT_FORMAT = "com.alphax.commons.error.invalidMessageTextFormat";
    
    private final MessageStorage messageStorage;
    private String defaultMessageSource;
    private final ErrorServiceControl control = new ErrorServiceControl();
    
    /**
     * Cache to hold loaded ResourceBundles. The key to this map is bundle basename
     * which holds a Map which has the locale as the key and in turn holds the
     * ResourceBundle instances.
     */
    private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new ConcurrentHashMap<>();

    
    /**
     * List of base names that are used to load resource bundles for localized
     * texts.
     */
    private final List<String> baseNames = new ArrayList<>(2);
    
    /**
     * Recommended way how to get an instance of ErrorService for your application.
     *
     * @return Error service that uses common messages and messages from default
     *         resource file (messages.yml).
     */
    public static MessageService getDefault() {
    	MessageServiceImpl errorService = new MessageServiceImpl("/" + DEFAULT_MESSAGES_BASENAME + YAML_EXTENSION);
        errorService.addResourceBundleBaseName(DEFAULT_MESSAGES_BASENAME);
        return errorService;
    }
    
    
    /**
     * Constructor that creates empty message storage.
     */
    public MessageServiceImpl() {
        messageStorage = new MessageStorage();
    }
    
    
    /**
     * Constructor that creates common messages and messages from file.
     *
     * @param messagesFilePath path to file with messages.
     */
    public MessageServiceImpl(String messagesFilePath) {
        this();
        loadMessages(messagesFilePath);
    }
    
    @Override
    public void addResourceBundleBaseName(String baseName) {
        if (!baseNames.contains(baseName)) {
            baseNames.add(baseName);
        }
    }
    
    
    @Override
    public Message createApiMessage(String key, Object... parameters) {
        Message message = createMessage(null, key, parameters);
//        return new BasicApiMessage(Collections.singletonList(message));
        return message;
    }
    
    
    @Override
    public Message createApiMessage(Locale locale, String key, Object... parameters) {
        Message message = createMessage(locale, key, parameters);
 //       return new BasicApiMessage(Collections.singletonList(message));
        return message;
    }


    @Override
    public String getDefaultMessageSource() {
        return defaultMessageSource;
    }
    
    
    @Override
    public void setDefaultMessageSource(String defaultMessageSource) {
        this.defaultMessageSource = defaultMessageSource;
    }
    
    
    @Override
    public void loadMessages(String messagesFilePath) {
        try (InputStream in = MessageServiceImpl.class.getResourceAsStream(messagesFilePath)) {
            Yaml yaml = new Yaml();
            ErrorMessages applicationMessages = yaml.loadAs(in, ErrorMessages.class);
            messageStorage.addMessages(applicationMessages);
        } catch (YAMLException | IOException e) {
        	log.error("There is problem with reading application messages file: " + messagesFilePath, e);
        }
    }
    
    
    private Message createMessage(Locale locale, String key, Object... parameters) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        
        ErrorMessage message = messageStorage.getErrorMessage(key);
        Object[] messageParameters = parameters;

        String text;
        StringBuilder sb = new StringBuilder();
        try (Formatter formatter = new Formatter(sb, locale)) {
            formatter.format(localizedText(locale, key + ".text", message.getText()), messageParameters);
            text = sb.toString();
        }catch (IllegalFormatConversionException exception) {
            log.debug("Internal error: Invalid message format was used", exception);
            message = messageStorage.getErrorMessage(INVALID_MESSAGE_TEXT_FORMAT);
            text = String.format(message.getText(), messageParameters);
        } 
      
        return new BasicMessage(message.getType(), message.getNumber(), text,
                key, locale.getLanguage().toUpperCase());
    }
    
    
    private String localizedText(Locale locale, String key, String defaultText) {
        if (locale != null) {
            for (int i = baseNames.size() -1; i >= 0; i--) {
                String baseName = baseNames.get(i);
                ResourceBundle bundle = getResourceBundle(baseName, locale);
                if (bundle == null) {
                    continue;
                }
                try {
                    return bundle.getString("messages." + key);
                } catch (MissingResourceException ignored) {
                    return defaultText;
                }
            }
        }
        return defaultText;
    }
    
    
    private ResourceBundle getResourceBundle(String baseName, Locale locale) {
        Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(baseName);
        if (localeMap != null) {
            ResourceBundle bundle = localeMap.get(locale);
            if (bundle != null) {
                return bundle;
            }
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, control);
            if (localeMap == null) {
                localeMap = new ConcurrentHashMap<>();
                Map<Locale, ResourceBundle> existing = this.cachedResourceBundles.putIfAbsent(baseName, localeMap);
                if (existing != null) {
                    localeMap = existing;
                }
            }
            localeMap.put(locale, bundle);
            return bundle;
        } catch (MissingResourceException ex) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("ResourceBundle '%s' not found: %s", baseName, ex.getMessage()));
            }
            return null;
        }
    }
    
    
    @Override
    public String getReadableMessage(String key, Object... parameters) {
        return createApiMessage(key, parameters).toReadableText();
    }

    /**
     * Custom implementation of {@code ResourceBundle.Control}, adding support for
     * UTF-8.
     */
    class ErrorServiceControl extends ResourceBundle.Control {
        protected ResourceBundle loadBundle(Reader reader) throws IOException {
            return new PropertyResourceBundle(reader);
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            if (format.equals("java.properties")) {
                return newJavaPropertiesBundle(baseName, locale, loader);
            } else {
                return super.newBundle(baseName, locale, format, loader, reload);
            }
        }

        private ResourceBundle newJavaPropertiesBundle(String baseName, Locale locale, ClassLoader loader)
                throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ClassLoader classLoader = loader;
            InputStream inputStream;
            try {
                inputStream = AccessController.doPrivileged((PrivilegedExceptionAction<InputStream>) () -> {
                    InputStream is = null;
                    is = classLoader.getResourceAsStream(resourceName);
                    return is;
                });
            } catch (PrivilegedActionException ex) {
                throw (IOException) ex.getException();
            }
            return loadBundleFromInputStream(inputStream);
        }

        private ResourceBundle loadBundleFromInputStream(InputStream inputStream) throws IOException {
            if (inputStream != null) {
                try (InputStreamReader bundleReader = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1)) {
                    return loadBundle(bundleReader);
                }
            } else {
                return null;
            }
        }
    }
    
    
}