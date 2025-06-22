package vn.edu.tlu.group23.mybakeryapp.models;

public class Task {
    private int id;
    private int shiftId;
    private String title;
    private String description;
    private String assignee;
    private String status;
    private String priority;

    // Constructor đầy đủ (dùng khi lấy từ database)
    public Task(int id, int shiftId, String title, String description, String assignee, String status, String priority) {
        this.id = id;
        this.shiftId = shiftId;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
    }

    // Constructor không có id (dùng khi thêm mới)
    public Task(int shiftId, String title, String description, String assignee, String status, String priority) {
        this.shiftId = shiftId;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getShiftId() {
        return shiftId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
