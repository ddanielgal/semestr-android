package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Semester {
    @PrimaryKey(autoGenerate = true) public Long id;
    public int number;
    public String university;
    public String degree;

}