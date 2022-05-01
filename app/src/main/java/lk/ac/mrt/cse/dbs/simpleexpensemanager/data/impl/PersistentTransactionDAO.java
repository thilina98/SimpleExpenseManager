package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String ACCOUNT_NO = "Account_No";
    public static final String ID = "ID";
    public static final String TYPE = "Type";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTransactionTableStatement = "CREATE TABLE Transactions(" + ID + " PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NO + " TEXT NOT NULL, " + TYPE + " TEXT NOT NULL, " + AMOUNT + " INT NOT NULL, " + DATE + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public PersistentTransactionDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String stringDate = new SimpleDateFormat("dd-mm-yyyy").format(date);
        String type = expenseType.equals(0)?"Expense":"Income";
        cv.put(ACCOUNT_NO, accountNo);
        cv.put(TYPE, type);
        cv.put(DATE, stringDate);

        db.insert("Transaction", null, cv);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
