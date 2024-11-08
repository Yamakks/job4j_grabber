package ru.job4j.gc.leak;

import java.util.List;

public class Post {

    private int id;

    private String text;

    private List<Comment> comments;

    public Post(int id, String text, List<Comment> comments) {
        this.id = id;
        this.text = text;
        this.comments = comments;
    }

    public Post(String text, List<Comment> comments) {
        this.text = text;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", text='" + text + '\''
                + ", comments=" + comments
                + '}';
    }
}
