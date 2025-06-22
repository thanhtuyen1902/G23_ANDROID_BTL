package vn.edu.tlu.group23.mybakeryapp.models;

public class Shift {
    private int id;
    private String name;
    private String startTime;
    private String endTime;

    public Shift(int id, String name, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

}
