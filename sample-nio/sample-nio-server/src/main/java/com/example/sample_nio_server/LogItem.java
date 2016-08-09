package com.example.sample_nio_server;

/**
 * Created by shungao on 3/17/15.
 */
public class LogItem {
    private boolean showTime;
    private long timeStamp;
    private String title;
    private String description;
    private String details;

    public LogItem(String title, String description) {
        this(title, description, "");
    }

    public LogItem(String title, String description, boolean showTime) {
        this(title, description, "");
        this.showTime = showTime;
    }

    public LogItem(String title, String description, String details) {
        showTime = false;
        timeStamp = System.currentTimeMillis();
        setTitle(title);
        setDescription(description);
        setDetails(details);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "LogItem{" +
                "timeStamp=" + timeStamp +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
//                ", details='" + details + '\'' +
                '}' + "\n";
    }
}
