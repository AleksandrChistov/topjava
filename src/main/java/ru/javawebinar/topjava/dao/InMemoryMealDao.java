package ru.javawebinar.topjava.dao;

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
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal create(Meal meal) {
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
