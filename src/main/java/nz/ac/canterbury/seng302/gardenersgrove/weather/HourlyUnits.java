package nz.ac.canterbury.seng302.gardenersgrove.weather;

@java.lang.SuppressWarnings({"java:S100", "java:S116", "java:S117"})
public class HourlyUnits {
    private String time;
    private String temperature_2m;
    private String weather_code;

    @Override
    public String toString() {
        return "HourlyUnits{" +
                "time='" + time + '\'' +
                ", temperature_2m='" + temperature_2m + '\'' +
                ", weather_code='" + weather_code + '\'' +
                '}';
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemperature_2m(String temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public void setWeather_code(String weather_code) {
        this.weather_code = weather_code;
    }
}
