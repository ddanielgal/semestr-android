package hu.danielgaldev.semestr.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hu.danielgaldev.semestr.model.pojo.RequirementType;

@Dao
public interface RequirementTypeDao {

    @Query("SELECT * FROM requirementtype")
    List<RequirementType> getAll();

    @Query("SELECT * FROM requirementtype WHERE id=:reqTypeId")
    RequirementType getById(final Long reqTypeId);

    @Insert
    Long insert(RequirementType reqt);

    @Update
    void update(RequirementType reqt);

    @Delete
    void deleteItem(RequirementType reqt);
}
