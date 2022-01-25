package org.shatranj.diwana.shatranjserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class StrategyNote {
    @Id
    @GenericGenerator(name = "note_id", strategy = "org.shatranj.diwana.shatranjserver.model.SequenceGenerator")
    @GeneratedValue(generator = "note_id")
    @Column(name="note_id")
    private String id;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private User user;

    @NotBlank
    private String message;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    public StrategyNote() {
    }

    public StrategyNote(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
