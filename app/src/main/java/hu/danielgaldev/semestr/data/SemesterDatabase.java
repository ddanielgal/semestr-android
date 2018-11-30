package hu.danielgaldev.semestr.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(
        entities = {Semester.class},
        version = 1
)

public abstract class SemesterDatabase extends RoomDatabase {
    public abstract SemesterItemDao semesterItemDao();
}