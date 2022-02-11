package org.frc.team5409.robot.training.robot;

public class Range {
    private final double _min;
    private final double _max;

    public Range(double v1, double v2) {
        if (v1 > v2) {
            _max = v1;
            _min = v2;
        } else {
            _max = v2;
            _min = v1;
        }
    }

    public Range(Range other) {
        _max = other._max;
        _min = other._min;
    }

    public double min() {
        return _min;
    }

    public double max() {
        return _max;
    }

    public double center() {
        return (_min + _max) / 2;
    }

    public boolean contains(double value, boolean inclusive) {
        return (inclusive) ? (value <= _max && value >= _min) : (value < _max && value > _min);
    }

    public double limit(double value) {
        return (value > _max) ? _max : (value < _min ? _min : value);
    }

    public boolean contains(double value) {
        return contains(value, true);
    }
}
