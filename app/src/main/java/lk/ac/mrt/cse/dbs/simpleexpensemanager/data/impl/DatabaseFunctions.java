package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseFunctions extends SQLiteOpenHelper {
    public static final String ACCOUNT_NO = "Account_No";
    public static final String HOLDER = "Holder";
    public static final String BANK = "Bank";
    public static final String BALANCE = "Balance";
    public static final String ID = "ID";
    public static final String TYPE = "Type";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";

    public DatabaseFunctions(@Nullable Context context) {
        super(context, "190238U.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement = "CREATE TABLE Account(" + ACCOUNT_NO + " TEXT PRIMARY KEY, " + HOLDER + " TEXT NOT NULL, " + BANK + " TEXT NOT NULL, " + BALANCE + " INT NOT NULL)";
        String createTransactionTableStatement = "CREATE TABLE Transactions(" + ID + " PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NO + " TEXT NOT NULL, " + TYPE + " TEXT NOT NULL, " + AMOUNT + " INT NOT NULL, " + DATE + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(createAccountTableStatement);
        sqLiteDatabase.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
