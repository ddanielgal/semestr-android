package hu.danielgaldev.semestr.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hu.danielgaldev.semestr.model.pojo.Semester;

@Dao
public interface SemesterDao {
    @Query("SELECT * FROM Semester")
    List<Semester> getAll();

    @Insert
    long insert(Semester semester);

    @Update
    void update(Semester semester);

    @Delete
    void deleteItem(Semester semester);
}