package ru.alishev.springcourse.third.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alishev.springcourse.third.service.SensorService;

@Controller
@RequestMapping("/test")
public class TestController {
    private final SensorService sensorService;

    @Autowired
    public TestController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("sensor", sensorService.findAll());


        //Кастомные запросы к контроллеру через сервис
        /*
        itemService.findByItemName("Airpods");
        itemService.findByOwner(peopleService.findAll().get(0));
        peopleService.test();
        */
        return "test/test";
    }

    @GetMapping("/auth")
    public String login() {
//        model.addAttribute("sensor", sensorService.findAll());


        //Кастомные запросы к контроллеру через сервис
        /*
        itemService.findByItemName("Airpods");
        itemService.findByOwner(peopleService.findAll().get(0));
        peopleService.test();
        */
        return "test/authentication";
    }
}
