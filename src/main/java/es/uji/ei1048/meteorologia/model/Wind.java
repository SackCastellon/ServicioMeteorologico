package es.uji.ei1048.meteorologia.model;

public final class Wind {
    private final float speed;
    private final float degrees;

    public Wind(float speed, float degrees) {
        this.speed = speed;
        this.degrees = degrees;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDegrees() {
        return degrees;
    }
}
