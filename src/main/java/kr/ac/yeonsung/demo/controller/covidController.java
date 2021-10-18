package kr.ac.yeonsung.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class covidController {
    @GetMapping("/covid")
    public String covid(){
        return "covid";
    }
}
