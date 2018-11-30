package hu.danielgaldev.semestr.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SemesterItemDao {
    @Query("SELECT * FROM Semester")
    List<Semester> getAll();

    @Insert
    long insert(Semester semester);

    @Update
    void update(Semester semester);

    @Delete
    void deleteItem(Semester semester);
}