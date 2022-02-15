package org.frc.team5409.robot.training.robot;

import org.jetbrains.annotations.Nullable;

public class Setpoint {
    private final Setpoint _parent;
    private final double _target;
    private final Range _range;

    public Setpoint(double target, Range range) {
        this(null, target, range);
    }

    public Setpoint(@Nullable Setpoint parent, double target, Range range) {
        _parent = parent;
        _target = target;
        _range = range;
    }
    
    @Nullable
    public Setpoint getParent() {
        return _parent;
    }

    public Range getRange() {
        return _range;
    }

    public double getTarget() {
        return _target;
    }

    public Setpoint branch(boolean isLeft) {
        Range range = new Range(
            (isLeft) ? _range.min() : _range.max(),  
            _target
        );
        
        return new Setpoint(this, range.center(), range);
    }
}
