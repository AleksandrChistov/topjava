package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.function.Consumer;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), admin, guest);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        User newUser = UsersUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void registerWithException() throws Exception {
        Consumer<UserTo> consumer = userTo -> {
            try {
                perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(userTo)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        UserTo newTo = new UserTo(null, "n", "newemail@ya.ru", "newPassword", 1500);
        consumer.accept(newTo);
        UserTo newTo2 = new UserTo(null, "newName", "newemail_ya.ru", "newPassword", 1500);
        consumer.accept(newTo2);
        UserTo newTo3 = new UserTo(null, "newName", "newemail@ya.ru", "newP", 1500);
        consumer.accept(newTo3);
        UserTo newTo4 = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 10001);
        consumer.accept(newTo4);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerWithDuplicateException() throws Exception {
        UserTo newTo = new UserTo(null, "newName", user.getEmail(), "newPassword", 1500);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Пользователь с таким email уже существует")));
    }

    @Transactional
    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UsersUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void updateWithException() throws Exception {
        Consumer<UserTo> consumer = userTo -> {
            try {
                perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(userHttpBasic(user))
                        .content(JsonUtil.writeValue(userTo)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        UserTo updatedTo = new UserTo(null, "n", "newemail@ya.ru", "newPassword", 1500);
        consumer.accept(updatedTo);
        UserTo updatedTo2 = new UserTo(null, "newName", "newemail_ya.ru", "newPassword", 1500);
        consumer.accept(updatedTo2);
        UserTo updatedTo3 = new UserTo(null, "newName", "newemail@ya.ru", "newP", 1500);
        consumer.accept(updatedTo3);
        UserTo updatedTo4 = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 10001);
        consumer.accept(updatedTo4);
    }

    @Test
    void getWithMeals() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-meals")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_MEALS_MATCHER.contentJson(user));
    }
}