package ru.javawebinar.topjava.web;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    static AtomicInteger id = new AtomicInteger(1);

    public static int authUserId() {
        return id.get();
    }

    public static void setAuthUserId(int newId) {
        id.set(newId);
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}