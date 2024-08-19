package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Time;
import java.sql.Date;

/**
 * Entity class representing the temperature ent through from the garden monitor
 * Represents the date, time, and temperature reading.
 */
@Entity(name="temperature")
public class Temperature {
    /**
     * Temperature will have an ID number that is unique and exists
     */
    @Id
    @Column(nullable = false)
    private long id;

    /**
     * Date the reading was taken
     */
    @Column(nullable = false)
    private Date date;

    /**
     * Time the reading was taken
     */
    @Column
    private Time time;

    /**
     * Temperature read from sensor
     */
    @Column
    private double temperature;

    /**
     * JPA Empty Constructor
     */
    protected Temperature() {}

    /**
     * Temperature constructor to intialize temperature data
     */
    public Temperature(Date date, Time time, double temperature) {
        this.date = date;
        this.time = time;
        this.temperature = temperature;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
    public Time getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public String toString() {
        return String.format(
                "Temperature entity {Date: %s Time: %s Temperature: %s",
                date, time, temperature
        );
    }
}
