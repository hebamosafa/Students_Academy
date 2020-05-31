package com.example.mohamedelwarraky.students.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mohamedelwarraky.students.data.StudentContract.StudentEntry;

/**
 * Database helper for Students app. Manages database creation and version management.
 */
public class StudentDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = StudentDbHelper.class.getSimpleName();


    /** Name of the database file */
    private static final String DATABASE_NAME = "student.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link StudentDbHelper}.
     *
     * @param context of the app
     */
    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table
      final String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
                + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentEntry.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
                + StudentEntry.COLUMN_STUDENT_GROUP + " TEXT, "
                + StudentEntry.COLUMN_STUDENT_COST + " FLOAT, "
                + StudentEntry.COLUMN_STUDENT_SCHOOL + " TEXT, "
                + StudentEntry.COLUMN_STUDENT_DAYS + " TEXT, "
                + StudentEntry.COLUMN_STUDENT_TEL + " TEXT, "
                + StudentEntry.COLUMN_STUDENT_DEGREE + " TEXT );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STUDENTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
