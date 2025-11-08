package com.example.arkanoid.models.Power;

public interface PowerFactory {
    Power<?> createPowerUp(String type, double x, double y);
}
