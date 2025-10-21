package org.howard.edu.lsp.midterm.question4;

public class Thermostat{
    private double temperatureC;
    private String id;
    private String location;
    private boolean connected;

    public Thermostat(String id, String location, double initialTempC) {
        this.id = id;
        this.location = location;
        this.temperatureC = initialTempC;
        this.connected = false;
    }

    public String getId() { return id; }

    public String getLocation() { return location; }

    public void setConnected(boolean connected) { this.connected = connected; }

    public double getTemperatureC() { return temperatureC; }

    public void setTemperatureC(double temperatureC) { this.temperatureC = temperatureC; }

    // Networked
    public void connect() { setConnected(true); }

    public void disconnect() { setConnected(false); }

    public boolean isConnected() { return this.connected; }

    // Status
    public String getStatus() {
        String connStatus = isConnected() ? "up" : "down";
        return "Thermostat[id=" + getId() + ", loc=" + getLocation() +
               ", conn=" + connStatus + ", tempC=" + temperatureC + "]";
    }
}
