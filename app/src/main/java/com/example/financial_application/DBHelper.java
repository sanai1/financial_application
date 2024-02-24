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
    public static final String COLUMN_UID = "uid", COLUMN_IS_EXPENSE = "is_expense", COLUMN_IS_BIG_PURCHASE = "is_big_purchase",
            COLUMN_SUMMA = "summa",  COLUMN_ADD_DATA = "add_data", COLUMN_CATEGORY_UID = "category_uid";


    public static final String TABLE_GOAL = "goal";
    public static final String COLUMN_NAME = "name_goal", COLUMN_SUMMA_GOAL = "summa", COLUMN_START_CAPITAL = "start_capital",
            COLUMN_PERCENT = "percent", COLUMN_INFLATION = "inflation";


    public static final String TABLE_CAPITAL = "capital";
    public static final String COLUMN_MONTH = "month", COLUMN_CAPITAL_SUM = "capital_sum";


    public static final String TABLE_CALCULATION_INFO = "calculation_info";
    public static final String COLUMN_DATE_CALCULATION = "date_calculation", COLUMN_TEMP_GOAL = "temp_goal",
            COLUMN_DATE_FINISH = "date_finish", COLUMN_PERCENT_DATE = "percent_date";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String command_one = "create table " + TABLE_HISTORY + "(" +
                COLUMN_UID + " integer," +
                COLUMN_IS_EXPENSE + " integer," +
                COLUMN_IS_BIG_PURCHASE + " integer," +
                COLUMN_SUMMA + " real," +
                COLUMN_ADD_DATA + " text," +
                COLUMN_CATEGORY_UID + " integer)";
        db.execSQL(command_one);

        String command_two = "create table " + TABLE_CATEGORY + "(" +
                COLUMN_CATEGORY_T_C + " text," +
                COLUMN_EXPENSE + " integer)";
        db.execSQL(command_two);

        String command_three = "create table " + TABLE_GOAL + "(" +
                COLUMN_NAME + " text," +
                COLUMN_SUMMA_GOAL + " integer," +
                COLUMN_START_CAPITAL + " integer," +
                COLUMN_PERCENT + " integer," +
                COLUMN_INFLATION + " integer)";
        db.execSQL(command_three);

        String command_four = "create table " + TABLE_CAPITAL + "(" +
                COLUMN_MONTH + " text," +
                COLUMN_CAPITAL_SUM + " real)";
        db.execSQL(command_four);

        String command_five = "create table " + TABLE_CALCULATION_INFO + "(" +
                COLUMN_DATE_CALCULATION + " text, " +
                COLUMN_TEMP_GOAL + " integer, " +
                COLUMN_DATE_FINISH + " text, " +
                COLUMN_PERCENT_DATE + " real)";
        db.execSQL(command_five);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            String command_one = "drop table if exists " + TABLE_HISTORY;
            db.execSQL(command_one);

            String command_two = "drop table if exists " + TABLE_CATEGORY;
            db.execSQL(command_two);

            String command_three = "drop table if exists " + TABLE_GOAL;
            db.execSQL(command_three);

            String command_four = "drop table if exists " + TABLE_CAPITAL;
            db.execSQL(command_four);

            String command_five = "drop table if exists " + TABLE_CALCULATION_INFO;
            db.execSQL(command_five);

            onCreate(db);
        }
    }
}
