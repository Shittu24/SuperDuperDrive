package com.udacity.jwdnd.course1.cloudstorage.contoller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "error"; // this is the name of the error.html file
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
