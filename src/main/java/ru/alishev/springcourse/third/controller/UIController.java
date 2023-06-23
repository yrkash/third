package ru.alishev.springcourse.third.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alishev.springcourse.third.service.SensorService;

@Controller
@RequestMapping("/ui")
public class UIController {
    private final SensorService sensorService;

    @Autowired
    public UIController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping()
    public String sendMeasurement(Model model, @RequestParam(value = "jwtToken") String jwtToken) {
        model.addAttribute("jwtToken", jwtToken);

        return "ui/send";
    }

    @GetMapping("/auth")
    public String login() {

        return "ui/auth";
    }
}
