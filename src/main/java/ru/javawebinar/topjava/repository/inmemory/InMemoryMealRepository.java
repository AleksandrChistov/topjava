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
import java.util.stream.IntStream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        int halfMealsSize = MealsUtil.meals.size() / 2;
        IntStream
                .range(0, MealsUtil.meals.size())
                .forEach(index -> save(index <= halfMealsSize ? 1 : 2, MealsUtil.meals.get(index)));
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
        return getValueOrNull(userId, meals -> {
            log.info("delete {}", id);
            return meals.remove(id);
        }) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return getValueOrNull(userId, meals -> {
            log.info("get {}", id);
            return meals.get(id);
        });
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getValueOrNull(userId, meals -> {
            log.info("getAll");
            return meals.values().stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        });
    }

    private <R> R getValueOrNull(int userId, Function<Map<Integer, Meal>, R> function) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return null;
        }
        return function.apply(meals);
    }
}
