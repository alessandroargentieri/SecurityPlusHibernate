package it.impresaconsulting.Gestic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UrlPathController {

    @RequestMapping("/gestic")
    public String goHome() {
        return "home.html";
    }
    @RequestMapping("/utenti")
    public String goUtenti() {
        return "home.html";
    }
    @RequestMapping("/anagrafica")
    public String goAnagrafica() {
        return "home.html";
    }
    @RequestMapping("/scadenze")
    public String goScadenze() {
        return "home.html";
    }
    @RequestMapping("/contact-us")
    public String goContatti() {
        return "home.html";
    }
}
