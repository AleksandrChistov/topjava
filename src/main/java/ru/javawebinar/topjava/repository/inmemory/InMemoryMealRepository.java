package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.USER_ID, meal));
        save(
                SecurityUtil.ADMIN_ID,
                new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 12, 45), "Обед", 500)
        );
        save(
                SecurityUtil.ADMIN_ID,
                new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 19, 0), "Ужин", 410)
        );
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        return getValueOrNull(userId, meals -> meals.remove(id)) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        return getValueOrNull(userId, meals -> meals.get(id));
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getValueOrNull(userId, meals -> filterByPredicate(meals, meal -> true));
    }

    @Override
    public List<Meal> getFiltered(LocalDate dateFrom, LocalDate dateTo, int userId) {
        log.info("getFilteredAll");
        return getValueOrNull(userId, meals -> filterByPredicate(meals, meal ->
                DateTimeUtil.isBetweenHalfOpen(meal.getDate(), dateFrom, dateTo.plusDays(1))
        ));
    }

    private <R> R getValueOrNull(int userId, Function<Map<Integer, Meal>, R> function) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return null;
        }
        return function.apply(meals);
    }

    private List<Meal> filterByPredicate(Map<Integer, Meal> meals, Predicate<Meal> filter) {
        return meals.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

