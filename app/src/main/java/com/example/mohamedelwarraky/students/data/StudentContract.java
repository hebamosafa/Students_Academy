package com.example.mohamedelwarraky.students.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Students app.
 */
public class StudentContract {

    /*
    static->to be global
    final->to be contstant
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.students";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    private  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.students/students/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_STUDENTS = "students";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private StudentContract() {
    }

    /**
     * Inner class that defines constant values for the students database table.
     * Each entry in the table represents a single student.
     */
    public static final class StudentEntry implements BaseColumns {

        /**
         * The content URI to access the student data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STUDENTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of students.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single student.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;

        /**
         * Name of database table for students
         */
        public final static String TABLE_NAME = "students";

        /**
         * Unique ID number for the student (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        /*
        *
        * */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_NAME = "name";

        /**
         * Name of group for the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_GROUP = "groups";

        /**
         * Name of school for the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_SCHOOL = "school";

        /**
         * Cost of the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_DAYS = "address";

        /**
         * Cost of the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_TEL = "phone";
        public final static String COLUMN_STUDENT_COST = "cost";




        /**
         * Degrees of the student.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_STUDENT_DEGREE = "degree";


    }


}
