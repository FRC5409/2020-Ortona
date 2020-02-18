package org.frc.team5409.robot.util;

public abstract class ErrorProfile {
    private double m_error;
    private double m_max_error;

    public ErrorProfile() {
        m_error = 0;
        m_max_error = 0;
    }

    public abstract void profile();

    public abstract void reset();

    protected final void setMaxError(double error) {
        m_max_error = error;
    }

    protected final void setError(double error) {
        m_error = error;
    }

    public final double getError() {
        return m_error;
    }

    public final boolean isAcceptable() {
        return m_error < m_max_error;
    }
}