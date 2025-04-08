package com.chatter.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TranslationService {
    private final MessageSource messageSource;

    public TranslationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * fetches a translated message for the given key and locale
     */
    public String getTranslation(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * fetches a translated message for the given key in the default locale
     */
    public String getTranslation(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
