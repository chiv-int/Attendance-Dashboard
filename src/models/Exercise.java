package models;

public class Exercise {
    private final String title;
    private final String description;
    
    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
}
