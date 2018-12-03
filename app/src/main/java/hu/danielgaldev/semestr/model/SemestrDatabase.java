package hu.danielgaldev.semestr.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import hu.danielgaldev.semestr.R;
import hu.danielgaldev.semestr.model.dao.RequirementDao;
import hu.danielgaldev.semestr.model.dao.RequirementTypeDao;
import hu.danielgaldev.semestr.model.dao.SubjectDao;
import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;
import hu.danielgaldev.semestr.model.pojo.Semester;
import hu.danielgaldev.semestr.model.dao.SemesterDao;
import hu.danielgaldev.semestr.model.pojo.Subject;

@Database(
        entities = {Semester.class, Subject.class, RequirementType.class, Requirement.class},
        version = 2,
        exportSchema = false
)

@TypeConverters(value = {Semester.University.class, Semester.Degree.class, Converters.class})
public abstract class SemestrDatabase extends RoomDatabase {

    private static SemestrDatabase INSTANCE;

    public abstract SemesterDao semesterDao();
    public abstract SubjectDao subjectDao();
    public abstract RequirementTypeDao reqTypeDao();
    public abstract RequirementDao reqDao();

    public synchronized static SemestrDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static SemestrDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                SemestrDatabase.class,
                "semestr-database")
                .build();
    }
}