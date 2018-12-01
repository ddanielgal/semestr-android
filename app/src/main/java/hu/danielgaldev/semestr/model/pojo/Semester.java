package hu.danielgaldev.semestr.model.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

@Entity
public class Semester {

    public enum University {
        BME, ELTE, CEU;

        @TypeConverter
        public static University getByOrdinal(int ordinal) {
            University ret = null;
            for (University uni : University.values()) {
                if (uni.ordinal() == ordinal) {
                    ret = uni;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(University uni) {
            return uni.ordinal();
        }
    }

    public enum Degree {
        BINFO, BVILLANY, MINFO, MVILLANY;

        @TypeConverter
        public static Degree getByOrdinal(int ordinal) {
            Degree ret = null;
            for (Degree deg : Degree.values()) {
                if (deg.ordinal() == ordinal) {
                    ret = deg;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(Degree deg) {
            return deg.ordinal();
        }
    }

    @PrimaryKey(autoGenerate = true) public Long id;
    public int number;
    public University university;
    public Degree degree;

    public Semester() {}

    public Semester(int number, University university, Degree degree) {
        this.number = number;
        this.university = university;
        this.degree = degree;
    }
}