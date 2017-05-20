package ie.seankehoe.buddyfit;

/**
 * Created by Sean Kehoe on 07/01/2017.
 */

public class DailyStats {
    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    private int steps;
    private int distance;
    private int floors;

    public int getHeartRatecardio() {
        return heartRatecardio;
    }

    public void setHeartRatecardio(int heartRatecardio) {
        this.heartRatecardio = heartRatecardio;
    }

    public int getHeartRatefatburn() {
        return heartRatefatburn;
    }

    public void setHeartRatefatburn(int heartRatefatburn) {
        this.heartRatefatburn = heartRatefatburn;
    }

    public int getHeartRateRest() {
        return heartRateRest;
    }

    public void setHeartRateRest(int heartRateRest) {
        this.heartRateRest = heartRateRest;
    }

    public int getHeartRatePeak() {
        return heartRatePeak;
    }

    public void setHeartRatePeak(int heartRatePeak) {
        this.heartRatePeak = heartRatePeak;
    }

    private int heartRateRest;
    private int heartRatefatburn;
    private int heartRatecardio;
    private int heartRatePeak;
    private int calories;

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

}
