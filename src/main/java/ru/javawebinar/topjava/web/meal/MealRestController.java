package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
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

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(authUserId(), id);
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getFilteredAll(String dateFrom, String dateTo, String timeFrom, String timeTo) {
        log.info("getFilteredAll");
        if (dateFrom.isEmpty() && dateTo.isEmpty() && timeFrom.isEmpty() && timeTo.isEmpty()) {
            return getAll();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime ltFrom = timeFrom.isEmpty() ? LocalTime.MIN : LocalTime.parse(timeFrom, tf);
        LocalTime ltTo = timeTo.isEmpty() ? LocalTime.MAX : LocalTime.parse(timeTo, tf);
        if (dateFrom.isEmpty() && dateTo.isEmpty() && (!timeFrom.isEmpty() || !timeTo.isEmpty())) {
            return MealsUtil.getFilteredTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY, ltFrom, ltTo);
        }
        LocalDate ldFrom = dateFrom.isEmpty() ? LocalDate.MIN : LocalDate.parse(dateFrom, df);
        LocalDate ldTo = dateTo.isEmpty() ? LocalDate.MAX : LocalDate.parse(dateTo, df);
        return MealsUtil.getFilteredTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY, ldFrom, ldTo, ltFrom, ltTo);
    }
}
