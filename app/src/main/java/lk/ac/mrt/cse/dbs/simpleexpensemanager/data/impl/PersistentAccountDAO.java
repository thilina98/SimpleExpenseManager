package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends SQLiteOpenHelper implements AccountDAO{
    public static final String ACCOUNT_TABLE = "Account";
    public static final String ACCOUNT_NO = "Account_No";
    public static final String HOLDER = "Holder";
    public static final String BANK = "Bank";
    public static final String BALANCE = "Balance";

    public PersistentAccountDAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement = "CREATE TABLE Account(" + ACCOUNT_NO + " TEXT PRIMARY KEY, " + HOLDER + " TEXT NOT NULL, " + BANK + " TEXT NOT NULL, " + BALANCE + " REAL NOT NULL)";
        sqLiteDatabase.execSQL(createAccountTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // it's not like we ever gonna update this app
    }


    @Override
    public List<String> getAccountNumbersList() {
        List<String> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                String acNo = cursor.getString(0);
                returnList.add(acNo);
            }while(cursor.moveToNext());
        }else{
            // nothing to add to the list
        }
        return returnList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACCOUNT_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do{
                String acNo = cursor.getString(0);
                String holder = cursor.getString(1);
                String bank = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account account = new Account(acNo, bank, holder, (double)balance);
                returnList.add(account);
            }while(cursor.moveToNext());
        }else{
            // will not add anything to the list.
        }

        cursor.close();
        sqLiteDatabase.close();

        return returnList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String queryString = "SELECT " + ACCOUNT_NO + " FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NO + "=" + accountNo;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        String acNo = cursor.getString(0);
        String holder = cursor.getString(1);
        String bank = cursor.getString(2);
        double balance = cursor.getDouble(3);

        Account account = new Account(acNo, bank, holder, (double)balance);

        cursor.close();
        sqLiteDatabase.close();

        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_NO, account.getAccountNo());
        cv.put(HOLDER, account.getAccountHolderName());
        cv.put(BANK, account.getBankName());
        cv.put(BALANCE, account.getBalance());

        sqLiteDatabase.insert(ACCOUNT_TABLE, null, cv);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String deleteEntryStatement = "DELETE FROM "+ ACCOUNT_NO+ " WHERE "+ ACCOUNT_NO+ "="+ accountNo;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(deleteEntryStatement);
        sqLiteDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String getBalanceQuery = "SELECT " + BALANCE + " FROM " + ACCOUNT_TABLE + " WHERE " + ACCOUNT_NO + "=" + accountNo;
        Cursor cursor = sqLiteDatabase.rawQuery(getBalanceQuery, null);

        String strBalance = cursor.getString(0);
        double balance = Double.parseDouble(strBalance);

        double newBalance;

        if (expenseType.equals(0)){
            newBalance = balance - amount;
        }else{
            newBalance = balance + amount;
        }

        String strNewBalance = new Double(newBalance).toString();
        String updateString = "UPDATE "+ ACCOUNT_TABLE + " SET " + BALANCE + "=" + strNewBalance + " WHERE " + ACCOUNT_NO + "=" + accountNo;

        sqLiteDatabase.execSQL(updateString);

        cursor.close();
        sqLiteDatabase.close();
    }
}
