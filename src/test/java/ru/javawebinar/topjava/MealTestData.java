package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = MEAL1_ID + 1;
    public static final int MEAL3_ID = MEAL2_ID + 1;
    public static final int MEAL4_ID = MEAL3_ID + 1;
    public static final int MEAL5_ID = MEAL4_ID + 1;
    public static final int MEAL6_ID = MEAL5_ID + 1;
    public static final int MEAL7_ID = MEAL6_ID + 1;
    public static final int MEAL8_ID = MEAL7_ID + 1;
    public static final int MEAL9_ID = MEAL8_ID + 1;
    public static final int ADMIN_MEAL_ID = MEAL9_ID + 1;
    public static final int NOT_FOUND_ID = 100;

    public static final LocalDateTime dateTimeOfMeal1 = LocalDateTime.of(2020, Month.JANUARY, 29, 19, 45);
    public static final LocalDateTime dateTimeOfMeal2 = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
    public static final LocalDateTime dateTimeOfMeal8 = LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0);
    public static final LocalDate dateInclusiveOfMeal2 = dateTimeOfMeal2.toLocalDate();
    public static final LocalDate dateInclusiveOfMeal8 = dateTimeOfMeal8.toLocalDate();
    public static final Meal meal1 = new Meal(MEAL1_ID, dateTimeOfMeal1, "Ужин", 385);
    public static final Meal meal2 = new Meal(MEAL2_ID, dateTimeOfMeal2, "Завтрак", 500);
    public static final Meal meal3 = new Meal(MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal4 = new Meal(MEAL4_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal5 = new Meal(MEAL5_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal meal6 = new Meal(MEAL6_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal7 = new Meal(MEAL7_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal meal8 = new Meal(MEAL8_ID, dateTimeOfMeal8, "Ужин", 410);
    public static final Meal meal9 = new Meal(MEAL9_ID, LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 615);
    public static final List<Meal> meals = Arrays.asList(meal9, meal8, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    public static final List<Meal> betweenUnclusiveMeals = Arrays.asList(meal8, meal7, meal6, meal5, meal4, meal3, meal2);

    public static Meal getNew() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Какая-то еда", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 29, 14, 0));
        updated.setDescription("Гамбургер");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
