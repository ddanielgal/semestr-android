package hu.danielgaldev.semestr.model.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import hu.danielgaldev.semestr.model.pojo.Requirement;
import hu.danielgaldev.semestr.model.pojo.RequirementType;

public interface RequirementDao {

    @Query("SELECT * FROM requirement WHERE requirementTypeId=:requirementTypeId")
    List<Requirement> getRequirementByType(final Long requirementTypeId);

    @Query("SELECT * FROM requirementtype WHERE id=:requirementTypeId")
    RequirementType getRequirementType(final Long requirementTypeId);

    @Insert
    long insert(Requirement requirement);

    @Update
    void update(Requirement requirement);

    @Delete
    void deleteItem(Requirement requirement);

}
