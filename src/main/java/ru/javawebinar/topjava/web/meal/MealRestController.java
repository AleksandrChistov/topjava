package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealTo create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        Meal createdMeal = service.create(authUserId(), meal);
        if (createdMeal == null) {
            return null;
        }
        // need to calculate excess
        return getAll().stream().filter(m -> Objects.equals(m.getId(), meal.getId())).findFirst().get();
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(authUserId(), id);
    }

    public MealTo get(int id) {
        log.info("get {}", id);
        service.get(authUserId(), id); // checkNotFoundWithId
        // need to calculate excess
        return getAll().stream().filter(m -> m.getId() == id).findFirst().get();
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}
