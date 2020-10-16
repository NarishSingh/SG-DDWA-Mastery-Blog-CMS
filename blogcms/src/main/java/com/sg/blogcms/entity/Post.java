package com.sg.blogcms.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private String title;
    private String body;
    private boolean approved;
    private boolean staticPage;
    private LocalDateTime createdOn;
    private LocalDateTime postOn;
    private LocalDateTime lastEdited;
    private LocalDateTime expireOn; //nullable
    private User user;

    /*gs*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isStaticPage() {
        return staticPage;
    }

    public void setStaticPage(boolean staticPage) {
        this.staticPage = staticPage;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getPostOn() {
        return postOn;
    }

    public void setPostOn(LocalDateTime postOn) {
        this.postOn = postOn;
    }

    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(LocalDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    public LocalDateTime getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(LocalDateTime expireOn) {
        this.expireOn = expireOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*testing*/
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + Objects.hashCode(this.body);
        hash = 97 * hash + (this.approved ? 1 : 0);
        hash = 97 * hash + (this.staticPage ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.createdOn);
        hash = 97 * hash + Objects.hashCode(this.postOn);
        hash = 97 * hash + Objects.hashCode(this.lastEdited);
        hash = 97 * hash + Objects.hashCode(this.expireOn);
        hash = 97 * hash + Objects.hashCode(this.user);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.approved != other.approved) {
            return false;
        }
        if (this.staticPage != other.staticPage) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.createdOn, other.createdOn)) {
            return false;
        }
        if (!Objects.equals(this.postOn, other.postOn)) {
            return false;
        }
        if (!Objects.equals(this.lastEdited, other.lastEdited)) {
            return false;
        }
        if (!Objects.equals(this.expireOn, other.expireOn)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title=" + title + ", body=" + body
                + ", approved=" + approved + ", staticPage=" + staticPage
                + ", createdOn=" + createdOn + ", postOn=" + postOn
                + ", lastEdited=" + lastEdited + ", expireOn=" + expireOn
                + ", user=" + user + '}';
    }

}
