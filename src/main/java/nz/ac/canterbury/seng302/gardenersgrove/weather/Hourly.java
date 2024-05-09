package nz.ac.canterbury.seng302.gardenersgrove.weather;

import java.util.List;

@java.lang.SuppressWarnings("all")
class Hourly {
    private List<String> time;
    private List<Double> temperature_2m;
    private List<Integer> weather_code;

    @Override
    public String toString() {
        return "Hourly{" +
                "time=" + time +
                ", temperature_2m=" + temperature_2m +
                ", weather_code=" + weather_code +
                '}';
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public void setTemperature_2m(List<Double> temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public void setWeather_code(List<Integer> weather_code) {
        this.weather_code = weather_code;
    }
}
