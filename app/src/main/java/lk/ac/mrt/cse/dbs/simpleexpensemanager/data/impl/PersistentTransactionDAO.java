package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String ACCOUNT_TABLE = "Account";
    public static final String TRANSACTION_TABLE = "`Transaction`";
    public static final String ACCOUNT_NO = "Account_No";
    public static final String HOLDER = "Holder";
    public static final String BANK = "Bank";
    public static final String BALANCE = "Balance";
    public static final String ID = "ID";
    public static final String TYPE = "Type";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";

    public PersistentTransactionDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement = "CREATE TABLE Account(" + ACCOUNT_NO + " TEXT PRIMARY KEY, " + HOLDER + " TEXT NOT NULL, " + BANK + " TEXT NOT NULL, " + BALANCE + " REAL NOT NULL)";
        sqLiteDatabase.execSQL(createAccountTableStatement);
        String createTransactionTableStatement = "CREATE TABLE `Transaction`(" + ID + " PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NO + " TEXT NOT NULL, " + TYPE + " TEXT NOT NULL, " + AMOUNT + " REAL NOT NULL, " + DATE + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String strDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        String type = expenseType.equals(ExpenseType.EXPENSE)?"Expense":"Income";
        cv.put(ACCOUNT_NO, accountNo);
        cv.put(TYPE, type);
        cv.put(DATE, strDate);
        cv.put(AMOUNT,amount);

        db.insert(TRANSACTION_TABLE, null, cv);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                String acNo = cursor.getString(1);
                String type = cursor.getString(2);
                String amount = cursor.getString(3);
                String strDate = cursor.getString(4);

                double intAmount = Double.parseDouble(amount);
                ExpenseType expenseType = type.equals("EXPENSE")?ExpenseType.EXPENSE:ExpenseType.INCOME;
                Date date;
                try {
                    date = new SimpleDateFormat("dd-MM-yyyy").parse(strDate);
                    Transaction transaction = new Transaction(date, acNo, expenseType, intAmount);
                    returnList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }while(cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return returnList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList = getAllTransactionLogs();
        int size = transactionsList.size();
        if (size <= limit) {
            return transactionsList;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactionsList.subList(size - limit, size);
    }
}
