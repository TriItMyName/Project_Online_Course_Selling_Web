package SpringBootBE.BackEnd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "enrollment")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EnrollmentID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourseID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Course course;

    @Column(name = "EnrollmentDate")
    private LocalDateTime enrollmentDate;

    @Column(name = "CompletionStatus")
    private String completionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrentLessonID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lesson currentLesson;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LessonProgress> lessonProgresses;

    @PrePersist
    void applyDefaults() {
        if (enrollmentDate == null) {
            enrollmentDate = LocalDateTime.now();
        }
        if (completionStatus == null || completionStatus.isBlank()) {
            completionStatus = "Not Started";
        }
    }
}
