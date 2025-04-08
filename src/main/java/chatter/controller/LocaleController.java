package com.chatter.controller;

import com.chatter.i18n.TranslationService;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/i18n")
public class LocaleController {
    private final TranslationService translationService;

    public LocaleController(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * fetches a translated message baseson the accept-language header
     */
    @GetMapping("/message")
    public String getTranslation(@RequestParam String key, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return translationService.getTranslation(key, locale);
    }

}
