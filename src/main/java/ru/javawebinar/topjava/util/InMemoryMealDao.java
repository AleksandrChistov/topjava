package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.interfaces.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements MealDao {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger(0);

    public InMemoryMealDao() {
        List<Meal> meals = Arrays.asList(
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(count.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(meal -> mealsMap.put(meal.getId(), meal));
    }

    @Override
    public Meal save(Meal meal) {
        Meal newMeal = new Meal(count.incrementAndGet(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return mealsMap.put(newMeal.getId(), newMeal);
    }

    @Override
    public Meal update(Meal meal) {
        return mealsMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }

    @Override
    public Meal get(int id) {
        return mealsMap.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealsMap.values();
    }
}
