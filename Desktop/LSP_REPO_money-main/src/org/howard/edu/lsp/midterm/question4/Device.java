package org.howard.edu.lsp.midterm.question4;

public abstract class Device {
    private String id;
    private String location;
    private boolean connected;

    public Device(String id, String location) {
        this.id = id;
        this.location = location;
        this.connected = false;
    }

    public String getId() { return id; }

    public String getLocation() { return location; }

    protected void setConnected(boolean connected) { this.connected = connected; }

    public boolean isConnected() { return connected; }
}
