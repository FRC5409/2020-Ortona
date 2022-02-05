package org.frc.team5409.robot.training.robot;

import org.jetbrains.annotations.Nullable;

public class Setpoint {
    private final Setpoint _parent;
    private final double _target;
    private final Range _range;
    private final SetpointType _type;

    public Setpoint(double target, Range range, SetpointType type) {
        this(null, target, range, type);
    }

    public Setpoint(@Nullable Setpoint parent, double target, Range range, SetpointType type) {
        _parent = parent;
        _target = target;
        _range = range;
        _type = type;
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

    public SetpointType getType() {
        return _type;
    }

    public Setpoint branch(boolean isLeft) {
        Range range = new Range(
            (isLeft) ? _range.min() : _range.max(),  
            _target
        );
        
        return new Setpoint(
            this, range.center(), range, 
            (isLeft) ? SetpointType.LEFT : SetpointType.RIGHT
        );
    }
}
