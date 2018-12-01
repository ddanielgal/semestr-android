package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

@Entity
public class RequirementType {

    @PrimaryKey(autoGenerate = true) public Long id;
    public int weight;
    public String name;

}