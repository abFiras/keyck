package com.example.microone.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class HyController {
    @GetMapping("/hy")
    public String Hy(){
            return "Micro one";
        }


}
