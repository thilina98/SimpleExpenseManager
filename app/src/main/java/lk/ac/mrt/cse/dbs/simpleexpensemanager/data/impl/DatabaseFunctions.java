package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseFunctions extends SQLiteOpenHelper {
    public DatabaseFunctions(@Nullable Context context) {
        super(context, "190238U.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryStatement = "CREATE TABLE Account(Account_No TEXT PRIMARY KEY, Holder TEXT NOT NULL, Bank TEXT NOT NULL, Balance INT NOT NULL)";

        sqLiteDatabase.execSQL(queryStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
