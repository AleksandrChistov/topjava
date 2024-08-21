package ru.javawebinar.topjava.web.meal;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class MealController extends AbstractMealController {
    static {
        log = LoggerFactory.getLogger(MealController.class);
    }
}