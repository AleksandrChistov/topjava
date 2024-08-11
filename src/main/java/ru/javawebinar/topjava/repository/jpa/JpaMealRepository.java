package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            Query query = em.createQuery("" +
                                         "UPDATE Meal m " +
                                         "SET m.description=:description, " +
                                         "m.calories=:calories, " +
                                         "m.dateTime=:date_time " +
                                         "WHERE m.id=:id AND " +
                                         "m.user.id=:user_id")
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("date_time", meal.getDateTime())
                    .setParameter("id", meal.id())
                    .setParameter("user_id", userId);
            try {
                if (query.executeUpdate() != 0) {
                    return meal;
                }
                throw new NoResultException();
            } catch (NoResultException e) {
                throw new NotFoundException(meal.getDescription() + " belongs to another user");
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
                .setParameter("id", id)
                .setParameter("user_id", userId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Query query = em.createQuery("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
                .setParameter("id", id)
                .setParameter("user_id", userId);

        try {
            return (Meal) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:user_id ORDER BY m.dateTime DESC", Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createQuery("" +
                              "SELECT m FROM Meal m " +
                              "WHERE m.user.id=:user_id " +
                              "AND m.dateTime>=:start_datetime " +
                              "AND m.dateTime<:end_dateTime " +
                              "ORDER BY m.dateTime DESC",
                        Meal.class)
                .setParameter("user_id", userId)
                .setParameter("start_datetime", startDateTime)
                .setParameter("end_dateTime", endDateTime)
                .getResultList();
    }
}