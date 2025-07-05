package com.ingressos.api.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class MessageConfig extends AcceptHeaderLocaleResolver {

    @SuppressWarnings("null")
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        @SuppressWarnings("deprecation")
        List<Locale> locales = Arrays.asList(new Locale("en"), new Locale("br"));
        String headerLanguage = request.getHeader("Accept-Language");
        return headerLanguage == null || headerLanguage.isEmpty() ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLanguage), locales);
    }

    @SuppressWarnings("deprecation")
    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("internationalization/messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setDefaultLocale(new Locale("br"));
        return source;
    }
}
