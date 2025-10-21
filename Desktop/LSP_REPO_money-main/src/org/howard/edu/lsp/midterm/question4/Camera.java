package org.howard.edu.lsp.midterm.question4;

public class Camera extends Device implements Networked, BatteryPowered {
    private int batteryPercent;

    public Camera(String id, String location, int initialBattery) {
        super(id, location);               // call Device constructor
        setBatteryPercent(initialBattery); // validate and initialize battery
    }

    // Networked interface methods
    @Override
    public void connect() {
        setConnected(true);
    }

    @Override
    public void disconnect() {
        setConnected(false);
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    // BatteryPowered interface methods
    @Override
    public int getBatteryPercent() {
        return batteryPercent;
    }

    @Override
    public void setBatteryPercent(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("battery 0..100");
        }
        this.batteryPercent = percent;
    }

    // Status method from Device
    public String getStatus() {
        String connStatus = isConnected() ? "up" : "down";
        return "Camera[id=" + getId() + ", loc=" + getLocation() +
               ", conn=" + connStatus + ", batt=" + batteryPercent + "%]";
    }
}
