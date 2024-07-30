package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealDao mealDao = new InMemoryMealDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        action = action == null ? "" : action;

        switch (action) {
            case "create": {
                Meal meal = new Meal(LocalDateTime.now(), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                break;
            }
            case "update": {
                String id = request.getParameter("id");
                Meal meal = mealDao.get(Integer.parseInt(id));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                break;
            }
            case "delete": {
                String id = request.getParameter("id");
                mealDao.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                break;
            }
            default:
                log.debug("getAll meals");
                request.setAttribute("meals", MealsUtil.filteredByStreams(new ArrayList<>(mealDao.getAll()), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String strId = request.getParameter("id");
        Integer id = strId.isEmpty() ? null : Integer.parseInt(strId);
        LocalDateTime date = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id == null) {
            Meal meal = new Meal(date, description, calories);
            mealDao.create(meal);
        } else {
            Meal meal = new Meal(id, date, description, calories);
            mealDao.update(meal);
        }
        response.sendRedirect("meals");
    }
}
