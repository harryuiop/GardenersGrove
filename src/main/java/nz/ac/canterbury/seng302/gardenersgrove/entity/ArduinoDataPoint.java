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
    private Double tempCelsius;

    @Column
    private Double humidityPercent;

    @Column
    private Double atmosphereAtm;

    @Column
    private Double lightPercent;

    @Column
    private Double moisturePercent;

    public ArduinoDataPoint(Garden garden, LocalDateTime time, Double tempCelsius, Double humidityPercent, Double atmosphereAtm, Double lightPercent, Double moisturePercent) {
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

    public Double getTempCelsius() {
        return tempCelsius;
    }

    public Double getHumidityPercent() {
        return humidityPercent;
    }

    public Double getAtmosphereAtm() {
        return atmosphereAtm;
    }

    public Double getLightPercent() {
        return lightPercent;
    }

    public Double getMoisturePercent() {
        return moisturePercent;
    }

}
