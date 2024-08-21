package ru.javawebinar.topjava.web.meal;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {
    static {
        log = LoggerFactory.getLogger(JspMealController.class);
    }

    @GetMapping
    public String get(Model model) {
        log.info("meals");
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        LocalDateTime ldt = LocalDateTime.parse((request.getParameter("dateTime")));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String idStr = request.getParameter("id");
        log.info("save {}", idStr);
        Integer id = idStr.isBlank() ? null : Integer.parseInt(idStr);
        Meal meal = new Meal(id, ldt, description, calories);
        if (id == null) {
            create(meal);
        } else {
            update(meal, id);
        }
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("create");
        model.addAttribute(
                "meal",
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)
        );
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(Model model, HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("update {}", id);
        model.addAttribute("meal", get(id));
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("delete {}", id);
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String getFiltered(Model model, HttpServletRequest request) {
        log.info("getFiltered");
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
