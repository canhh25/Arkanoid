package com.example.arkanoid.models.Power;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PowerImpl implements PowerFactory {
    private final Map<String, BiFunction<Double, Double, Power<?>>> init = new HashMap<>();

    public PowerImpl() {
        initPower();
    }
    private void initPower() {
        init.put("expand_paddle", ExpandPaddle::new);
        init.put("fast_ball", FastBall::new);
        init.put("multi_ball", MultiBallPower::new);
        init.put("extra_life", ExtraLifePower::new);
    }

    @Override
    public Power<?> createPowerUp(String type, double x, double y) {
        BiFunction<Double, Double, Power<?>> create = init.get(type);
        if (create != null) {
            return create.apply(x, y);
        }
        throw new IllegalArgumentException("Power type " + type);
    }

    public void customPower(String type, BiFunction<Double, Double, Power<?>> power) {
        init.put(type, power);
    }
}
