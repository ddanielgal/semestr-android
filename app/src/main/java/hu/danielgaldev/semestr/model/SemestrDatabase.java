package hu.danielgaldev.semestr.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import hu.danielgaldev.semestr.model.dao.SubjectDao;
import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.model.dao.SemesterDao;
import hu.danielgaldev.semestr.model.pojo.Subject;

@Database(
        entities = {Semester.class, Subject.class},
        version = 1,
        exportSchema = false
)

@TypeConverters(value = {Semester.University.class, Semester.Degree.class})
public abstract class SemestrDatabase extends RoomDatabase {
    public abstract SemesterDao semesterDao();
    public abstract SubjectDao subjectDao();
}