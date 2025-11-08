package com.example.arkanoid.models.Power;

public interface PowerStrategy<T> {
    void apply(T target);
    void remove(T target);
}
