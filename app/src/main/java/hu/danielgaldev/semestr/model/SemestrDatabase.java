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
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                SemestrDatabase db = getInstance(context);

                                Long semId = db.semesterDao().insert(
                                        new Semester(1, Semester.University.BME, Semester.Degree.BINFO)
                                );

                                // Subjects
                                List<Long> subjectIds = new ArrayList<>();
                                subjectIds.add(db.subjectDao().insert(new Subject("Prog1",6, semId)));
                                subjectIds.add(db.subjectDao().insert(new Subject("Bsz1",4, semId)));
                                subjectIds.add(db.subjectDao().insert(new Subject("Anal1",5, semId)));

                                // Requirement types
                                List<Long> reqTypeIds = new ArrayList<>();
                                reqTypeIds.add(db.reqTypeDao().insert(new RequirementType("Homework",7)));
                                reqTypeIds.add(db.reqTypeDao().insert(new RequirementType("ZH",7)));
                                reqTypeIds.add(db.reqTypeDao().insert(new RequirementType("Labor",2)));
                                reqTypeIds.add(db.reqTypeDao().insert(new RequirementType("Gyak",1)));
                                reqTypeIds.add(db.reqTypeDao().insert(new RequirementType("Vizsga",14)));

                                // Requirements
                                // Prog1
                                Date date = new Date(2019, 1, 4);
                                for (int i = 1; i < 8; i++) {
                                    db.reqDao().insert(new Requirement("Labor " + i, date, "", reqTypeIds.get(2), subjectIds.get(0)));
                                }
                                for (int i = 1; i < 13; i++) {
                                    db.reqDao().insert(new Requirement("Gyakorlat " + i, date, "", reqTypeIds.get(3), subjectIds.get(0)));
                                }
                                db.reqDao().insert(new Requirement("ZH1", date,"", reqTypeIds.get(1), subjectIds.get(0)));
                                db.reqDao().insert(new Requirement("ZH2", date, "", reqTypeIds.get(1), subjectIds.get(0)));
                                db.reqDao().insert(new Requirement("NHF", date, "", reqTypeIds.get(0), subjectIds.get(0)));
                                // Bsz1
                                db.reqDao().insert(new Requirement("ZH1", date, "", reqTypeIds.get(1), subjectIds.get(1)));
                                db.reqDao().insert(new Requirement("ZH2", date, "", reqTypeIds.get(1), subjectIds.get(1)));
                                db.reqDao().insert(new Requirement("Vizsga", date, "", reqTypeIds.get(4), subjectIds.get(1)));
                                // Anal1
                                for (int i = 1; i < 13; i++) {
                                    db.reqDao().insert(new Requirement("Gyakorlat " + i, date, "", reqTypeIds.get(3), subjectIds.get(2)));
                                }
                                db.reqDao().insert(new Requirement("ZH1", date, "", reqTypeIds.get(1), subjectIds.get(2)));
                                db.reqDao().insert(new Requirement("ZH2", date, "", reqTypeIds.get(1), subjectIds.get(2)));
                                db.reqDao().insert(new Requirement("Vizsga", date, "", reqTypeIds.get(4), subjectIds.get(2)));

                            }
                        });
                    }
                })
                .build();
    }
}