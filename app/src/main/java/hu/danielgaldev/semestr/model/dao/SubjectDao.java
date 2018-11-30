package hu.danielgaldev.semestr.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hu.danielgaldev.semestr.model.pojo.Subject;

@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subject WHERE semesterId=:semesterId")
    List<Subject> getSubjectsForSemester(final Long semesterId);

    @Insert
    long insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void deleteItem(Subject subject);
}
