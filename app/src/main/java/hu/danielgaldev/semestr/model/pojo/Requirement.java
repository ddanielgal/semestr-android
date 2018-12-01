package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = RequirementType.class,
        parentColumns = "id",
        childColumns = "requirementTypeId"))
public class Requirement {

    @PrimaryKey(autoGenerate = true) public Long id;
    public String name;
    public Date deadline;
    public Long requirementTypeId;

    public Requirement(String name, Date deadline, Long requirementTypeId) {
        this.requirementTypeId = requirementTypeId;
        this.deadline = deadline;
        this.name = name;
    }

}