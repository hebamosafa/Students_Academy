package com.example.mohamedelwarraky.students;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;


import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.mohamedelwarraky.students.data.StudentContract.StudentEntry;

/**
 * {@link StudentCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of student data as its data source. This adapter knows
 * how to create list items for each row of student data in the {@link Cursor}.
 */
public class StudentCursorAdapter extends CursorAdapter implements Filterable  {


    /**
     * Constructs a new {@link StudentCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public StudentCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summryTextView = view.findViewById(R.id.summary);
        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_NAME);
        int groupColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_GROUP);
        // Read the student attributes from the Cursor for the current student
        String studentName = cursor.getString(nameColumnIndex);
        String studentGroup = cursor.getString(groupColumnIndex);


        // Update the TextViews with the attributes for the current student
        nameTextView.setText(studentName);
        summryTextView.setText(studentGroup);
    }

}

