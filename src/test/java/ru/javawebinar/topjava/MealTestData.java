package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.GUEST_ID;

public class MealTestData {
    public static final int MEAL_ID = GUEST_ID + 1;
    public static final int NOT_FOUND_ID = 100;

    public static final LocalDateTime duplicateDateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
    public static final Meal meal = new Meal(MEAL_ID, duplicateDateTime, "Завтрак", 500);
    public static int MEAL_ID_COUNT = MEAL_ID;
    public static final List<Meal> meals = Arrays.asList(
            new Meal(MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(++MEAL_ID_COUNT, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static Meal getNew() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal);
        updated.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0));
        updated.setDescription("Гамбургер");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime").isEqualTo(expected);
    }
}
