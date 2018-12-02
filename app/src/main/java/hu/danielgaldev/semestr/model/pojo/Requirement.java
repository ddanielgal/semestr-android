package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = RequirementType.class,
        parentColumns = "id",
        childColumns = "requirementTypeId"),
        @ForeignKey(entity = Subject.class,
                parentColumns = "id",
                childColumns = "subjectId")
})
public class Requirement {

    @PrimaryKey(autoGenerate = true) public Long id;
    public String name;
    public Date deadline;
    public String details;
    public boolean completed;
    public Long requirementTypeId;
    public Long subjectId;

    public Requirement(String name, Date deadline, String details, Long requirementTypeId, Long subjectId) {
        this.requirementTypeId = requirementTypeId;
        this.deadline = deadline;
        this.name = name;
        this.subjectId = subjectId;
        this.details = details;
        this.completed = false;
    }

    @Ignore
    public Requirement(String name, Date deadline, String details, Long requirementTypeId, Long subjectId, boolean completed) {
        this.requirementTypeId = requirementTypeId;
        this.deadline = deadline;
        this.name = name;
        this.subjectId = subjectId;
        this.details = details;
        this.completed = completed;
    }

}