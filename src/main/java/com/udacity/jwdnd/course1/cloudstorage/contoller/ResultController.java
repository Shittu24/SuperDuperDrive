package com.udacity.jwdnd.course1.cloudstorage.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultController {

    @GetMapping("/result")
    public String resultView(@RequestParam("success") boolean success,
                             @RequestParam(name = "errorMessage", required = false) String errorMessage,
                             Model model) {
        model.addAttribute("success", success);
        if (!success) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "result";
    }
}
