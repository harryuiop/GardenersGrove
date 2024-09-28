package nz.ac.canterbury.seng302.gardenersgrove.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Formatted Result to send to graph view with labels for day, week, month views.
 */
public class FormattedGraphData {

    private final List<List<Double>> sensorReadings;
    private final List<String> labels;

    /**
     * @param sensorReadings Lists of readings for each sensor to be on x-axis of graph.
     * @param labels Time labels to be on y-axis of graph.
     */
    public FormattedGraphData(List<List<Double>> sensorReadings, List<String> labels) {
        this.sensorReadings = sensorReadings;
        this.labels = labels;
    }

    public List<List<Double>> getSensorReadings() {
        return sensorReadings;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Double> getTemperature() {
        if (sensorReadings.isEmpty()) {
            return new ArrayList<>();
        }
        return sensorReadings.get(0);
    }

    public List<Double> getHumidity() {
        if (sensorReadings.isEmpty()) {
            return new ArrayList<>();
        }
        return sensorReadings.get(1);
    }

    public List<Double> getAtmosphere() {
        if (sensorReadings.isEmpty()) {
            return new ArrayList<>();
        }
        return sensorReadings.get(2);
    }

    public List<Double> getLight() {
        if (sensorReadings.isEmpty()) {
            return new ArrayList<>();
        }
        return sensorReadings.get(3);
    }

    public List<Double> getMoisture() {
        if (sensorReadings.isEmpty()) {
            return new ArrayList<>();
        }
        return sensorReadings.get(4);
    }

    public void addAll(FormattedGraphData dayGraphData) {
        this.sensorReadings.addAll(dayGraphData.sensorReadings);
    }
}
