package net.langreader.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultRestController implements ErrorController {
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    @RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
    public String redirect() {
        return "forward:/";
    }

    @Override
    public String getErrorPath() {
        return "forward:/";
    }
}

