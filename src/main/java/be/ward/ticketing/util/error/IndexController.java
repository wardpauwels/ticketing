package be.ward.ticketing.util.error;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private final String PATH = "/api/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "Error Handling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
