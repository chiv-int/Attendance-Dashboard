package models;

public class Lesson {
    private final String title;
    private final String content;
    
    public Lesson(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
}
