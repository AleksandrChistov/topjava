package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private List<MealTo> list = getHardcodedMeals();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("open meals");
        request.setAttribute("meals", list);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private List<MealTo> getHardcodedMeals() {
        List<Meal> meals = new ArrayList<>();
        meals.add(new Meal(LocalDateTime.of(2024, Month.JULY, 20, 19, 0), "Eggs", 500));
        meals.add(new Meal(LocalDateTime.of(2024, Month.JULY, 20, 13, 15), "Bread", 800));
        meals.add(new Meal(LocalDateTime.of(2024, Month.JULY, 20, 7, 0), "Sausages", 1000));
        meals.add(new Meal(LocalDateTime.of(2024, Month.MAY, 1, 7, 30), "Soup", 350));
        meals.add(new Meal(LocalDateTime.of(2024, Month.MAY, 1, 12, 30), "Bread", 800));
        meals.add(new Meal(LocalDateTime.of(2024, Month.APRIL, 5, 9, 45), "Pork chop", 1500));
        return MealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(19, 1), CALORIES_PER_DAY);
    }
}
