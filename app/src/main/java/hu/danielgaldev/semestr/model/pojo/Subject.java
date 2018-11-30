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
    public Long id;
    public String name;
    public int credits;
    public final Long semesterId;

    public Subject(String name, int credits, Long semesterId) {
        this.credits = credits;
        this.name = name;
        this.semesterId = semesterId;
    }
}
