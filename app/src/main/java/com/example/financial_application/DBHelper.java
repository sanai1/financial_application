package com.example.financial_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_sqlite";

    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_CATEGORY_T_C = "category_t_c", COLUMN_EXPENSE = "expense";

    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_EXPENDITURE = "expenditure", COLUMN_CATEGORY_T_H = "category_t_h",
            COLUMN_SUM = "sum", COLUMN_BIG_PURCHASE = "dig_purchase", COLUMN_ADD_DATA = "add_data";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String command_one = "create table " + TABLE_HISTORY + "(" + COLUMN_CATEGORY_T_H + " text primary key," + COLUMN_EXPENDITURE + " integer,"
                + COLUMN_BIG_PURCHASE + " integer," + COLUMN_SUM + " real," + COLUMN_ADD_DATA + " text)";
        db.execSQL(command_one);

        String command_two = "create table " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_T_C + " text," + COLUMN_EXPENSE + " integer)";
        db.execSQL(command_two);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String command_one  = "drop table if exists " + TABLE_HISTORY;
        db.execSQL(command_one);

        String command_two = "drop table if exists " + TABLE_CATEGORY;
        db.execSQL(command_two);

        onCreate(db);
    }
}
