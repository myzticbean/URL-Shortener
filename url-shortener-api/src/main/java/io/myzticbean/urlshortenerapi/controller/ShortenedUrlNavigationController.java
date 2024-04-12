package io.myzticbean.urlshortenerapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ShortenedUrlNavigationController {

    @GetMapping("/{short-code}")
    public RedirectView navigateUrl(@PathVariable(value = "short-code") String shortCode) {
        System.out.println("Short code: " + shortCode);
        // service call to fetch the url
        return new RedirectView("https://www.google.com");
    }

}
