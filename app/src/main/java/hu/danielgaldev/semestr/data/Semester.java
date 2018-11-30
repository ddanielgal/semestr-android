package hu.danielgaldev.semestr.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "semester")
public class Semester {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "number")
    public int number;

    @ColumnInfo(name = "university")
    public String university;

    @ColumnInfo(name = "degree")
    public String degree;

}