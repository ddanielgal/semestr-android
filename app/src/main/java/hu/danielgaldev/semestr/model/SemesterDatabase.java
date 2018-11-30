package hu.danielgaldev.semestr.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.model.dao.SemesterDao;

@Database(
        entities = {Semester.class},
        version = 1,
        exportSchema = false
)

public abstract class SemesterDatabase extends RoomDatabase {
    public abstract SemesterDao semesterItemDao();
}