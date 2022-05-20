package dev.mlukas.langreader;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController implements ErrorController {
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    @SuppressWarnings("MVCPathVariableInspection")
    @RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
    public String redirect() {
        return "forward:/";
    }
}
