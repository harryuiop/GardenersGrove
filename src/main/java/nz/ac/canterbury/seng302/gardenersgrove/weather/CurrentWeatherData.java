package nz.ac.canterbury.seng302.gardenersgrove.weather;

public interface CurrentWeatherData {

     /**
      * Gets the description for the current weather
      * @return a string description
      */
     String getDescription();

     /**
      * Gets the current temperature
      * @return temperature in Celsius as a double
      */
     double getTemperature();

     /**
      * Gets the relative humidity
      * @return the relative humidity as an integer
      * indicating the percentage
      */
     int getHumidity();
}
