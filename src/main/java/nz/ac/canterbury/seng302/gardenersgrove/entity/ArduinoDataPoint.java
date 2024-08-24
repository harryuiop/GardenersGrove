package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity to save data collected from the sensors of an arduino every time they are sent to the system.
 */
@Entity
public class ArduinoDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Garden garden;

    @Column
    private LocalDateTime time;

    @Column
    private double tempCelsius;

    @Column
    private double humidityPercent;

    @Column
    private double atmosphereAtm;

    public Long getId() {
        return id;
    }

    public Garden getGarden() {
        return garden;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getTempCelsius() {
        return tempCelsius;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public double getAtmosphereAtm() {
        return atmosphereAtm;
    }

    public double getLightPercent() {
        return lightPercent;
    }

    public double getMoisturePercent() {
        return moisturePercent;
    }

    @Column
    private double lightPercent;

    @Column
    private double moisturePercent;

    public ArduinoDataPoint(Garden garden, LocalDateTime time, double tempCelsius, double humidityPercent, double atmosphereAtm, double lightPercent, double moisturePercent) {
        this.garden = garden;
        this.time = time;
        this.tempCelsius = tempCelsius;
        this.humidityPercent = humidityPercent;
        this.atmosphereAtm = atmosphereAtm;
        this.lightPercent = lightPercent;
        this.moisturePercent = moisturePercent;
    }

    public ArduinoDataPoint() {

    }

    public Long getId() {
        return id;
    }

    public Garden getGarden() {
        return garden;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getTempCelsius() {
        return tempCelsius;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public double getAtmosphereAtm() {
        return atmosphereAtm;
    }

    public double getLightPercent() {
        return lightPercent;
    }

    public double getMoisturePercent() {
        return moisturePercent;
    }

}
