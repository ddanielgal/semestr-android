package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Semester.class,
                parentColumns = "id",
                childColumns = "semesterId",
                onDelete = CASCADE
        )
)
public class Subject {
    @PrimaryKey(autoGenerate = true)
    public Long subjectId;
    public String website;
    public int credits;
    public final int semesterId;

    public Subject(Long subjectId, String website, int credits, int semesterId) {
        this.subjectId = subjectId;
        this.website = website;
        this.credits = credits;
        this.semesterId = semesterId;
    }
}
