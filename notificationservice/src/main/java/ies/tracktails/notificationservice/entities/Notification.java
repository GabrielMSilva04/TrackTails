package ies.tracktails.notificationservice.entities;

import jakarta.persistence.*;
import java.util.Date;

@Table(name = "notifications")
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

//    @Column(nullable = false)
//    private Date date;

    public Notification() {
        super();
    }

    public Notification(long userId, String title, String content/*, Date date*/) {
        super();
        this.userId = userId;
        this.title = title;
        this.content = content;
        //this.date = date;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

//    public Date getDate() {
//        return date;
//    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public void setDate(Date date) {
//        this.date = date;
//    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                //", date=" + date +
                '}';
    }
}
