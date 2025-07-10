package br.ufscar.dc.dsw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class LanguageController {
    // ... logger if you have it ...

    @PostMapping("/language")
    public String changeLanguage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("--- LanguageController: changeLanguage method entered ---"); // Add this line
        String newLocale = request.getParameter("lang");
        System.out.println("--- LanguageController: 'lang' parameter received: " + newLocale + " ---"); // Add this line

        if (newLocale != null) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver != null) {
                localeResolver.setLocale(request, response, new java.util.Locale(newLocale));
                System.out.println("--- LanguageController: Locale set to: " + newLocale + " ---"); // Add this line
            } else {
                System.err.println("--- LanguageController: LocaleResolver is NULL! ---"); // Add this line
            }
        } else {
            System.err.println("--- LanguageController: 'lang' parameter is NULL! ---"); // Add this line
        }

        String referer = request.getHeader("Referer");
        String redirectUrl = (referer != null ? referer : "/");
        System.out.println("--- LanguageController: Redirecting to: " + redirectUrl + " ---"); // Add this line
        return "redirect:" + redirectUrl;
    }
}