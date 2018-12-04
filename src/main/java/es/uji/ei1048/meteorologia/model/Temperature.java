package es.uji.ei1048.meteorologia.model;

public final class Temperature {
    private final float current;
    private final float min;
    private final float max;

    public Temperature(float current, float min, float max) {
        this.current = current;
        this.min = min;
        this.max = max;
    }

    public float getCurrent() {
        return current;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
