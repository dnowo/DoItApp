package io.github.dnowo.DoitApp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "jobs")
@RequiredArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer priority;
    private String title;
    @Lob
    @Column
    private String description;
    private LocalDateTime deadline;
    private Boolean notification;

    @Column(columnDefinition = "boolean default false")
    private Boolean ended;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", priority=" + priority +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", notification=" + notification +
                ", ended=" + ended +
                '}';
    }
}
