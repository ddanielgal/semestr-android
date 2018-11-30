package hu.danielgaldev.semestr.model;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private SemestrDatabase db;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        db = Room.databaseBuilder(mCtx, SemestrDatabase.class, "semestr-database").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public SemestrDatabase getDb() {
        return db;
    }
}
