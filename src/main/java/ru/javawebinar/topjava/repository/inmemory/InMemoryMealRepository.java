package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(0, meal));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        log.info("save {}", meal);
        Map<Integer, Meal> meals = repository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (meals == null) {
                meals = new ConcurrentHashMap<>();
                repository.put(userId, meals);
            }
            meals.put(meal.getId(), meal);
            return meal;
        }
        if (meals == null) {
            return null;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, int id) {
        return getValueOrNull(userId, meals -> {
            log.info("delete {}", id);
            return meals.remove(id) != null;
        });
    }

    @Override
    public Meal get(Integer userId, int id) {
        return getValueOrNull(userId, meals -> {
            log.info("get {}", id);
            return meals.get(id);
        });
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return getValueOrNull(userId, meals -> {
            log.info("getAll");
            return meals.values().stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        });
    }

    private <R> R getValueOrNull(Integer userId, Function<Map<Integer, Meal>, R> function) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return null;
        }
        return function.apply(meals);
    }
}

