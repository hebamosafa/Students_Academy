package com.example.mohamedelwarraky.students;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.mohamedelwarraky.students.data.StudentContract;
import com.example.mohamedelwarraky.students.data.StudentContract.StudentEntry;
import com.example.mohamedelwarraky.students.data.StudentDbHelper;


import java.io.File;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pet data loader
     */
    private static final int STUDENT_LOADER = 0;

    //to excel
    final String  path=Environment.getExternalStorageDirectory() + "academy.xls";
    File file = new File(path);

    /**
     * Adapter for the ListView
     */
    StudentCursorAdapter mCursorAdapter;
    Uri currentPetUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkPermission(Manifest.permission.READ_CONTACTS,107);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,103);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the student data
         ListView studentListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        studentListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new StudentCursorAdapter(this, null);
        studentListView.setAdapter(mCursorAdapter);
        studentListView.setTextFilterEnabled(true);


        // Prepare your adapter for filtering
        mCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {

                StudentDbHelper mDbHelper = new StudentDbHelper(getBaseContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();
                // in real life, do something more secure than concatenation
                // but it will depend on your schema
                // This is the query that will run on filtering
                String query =
                        "SELECT " + StudentEntry._ID + " , " + StudentEntry.COLUMN_STUDENT_NAME
                                + " , " + StudentEntry.COLUMN_STUDENT_GROUP
                                + " FROM " + StudentEntry.TABLE_NAME
                                + " where " + StudentEntry.COLUMN_STUDENT_NAME + " like '%" + constraint + "%' "
                                + " OR " + StudentEntry.COLUMN_STUDENT_GROUP + " like '%" + constraint + "%' "
                                + " ORDER BY NAME ASC";
                return db.rawQuery(query, null);
            }
        });

        // Setup the item click listener
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PetEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                 currentPetUri = ContentUris.withAppendedId(StudentEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);
                intent.putExtra("Id", id);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });


        // Kick off the loader
        getLoaderManager().initLoader(STUDENT_LOADER, null, this);
    }

                /////////////////////// DELETE_ALL
    private void deleteAllStudents() {
        int rowsDeleted = getContentResolver().delete(StudentEntry.CONTENT_URI, null, null);
        //((excel)  this.getApplication()).set_excel();

        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }
                               /////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) mItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String data) {
                // This is the filter in action
                mCursorAdapter.getFilter().filter(data.toString());
                // Show the chande when the user write something.
                mCursorAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return true;
    }
           /////////////////////////MENU//////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                showpopup();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteAllConfirmationDialog();
                return true;
            case R.id.action_excel:



                SQLiteToExcel sqliteToExcel= new SQLiteToExcel(this, "student.db",Environment.getExternalStorageDirectory().toString() );

                sqliteToExcel.exportSingleTable(StudentContract.StudentEntry.TABLE_NAME,"academy.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {


                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Error msg: " + e);
                        Toast.makeText(getApplicationContext(),"Failed"+e, Toast.LENGTH_LONG).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"Data saved", Toast.LENGTH_LONG).show();
//                Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setDataAndType(uri,"application/vnd.ms-excel");
//                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

             ///////////////////////////////LOADER
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                StudentEntry._ID,
                StudentEntry.COLUMN_STUDENT_NAME,
                StudentEntry.COLUMN_STUDENT_GROUP};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                StudentEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link StudentCursorAdapter} with this new cursor containing updated student data
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
                                   ////////////////////////
    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteAllConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllStudents();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


      ////////////////////////////////////////////////////////////////////////////////////////////////////////////


                     /////////////////////////////////////////////////////////////////////////


    public void showpopup(){
        Context mContext = getApplicationContext();

        // Get the activity
        //Activity mActivity = MainActivity.this;
        final PopupWindow mPopupWindow;

        // Get the widgets reference from XML layout
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.empty_view);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.pop,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
        Button setButton = (Button) customView.findViewById(R.id.ib_set);
        final EditText pass=(EditText)customView.findViewById(R.id.pass);

        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().trim().equals("011472")) {

                    String mytotal = String.valueOf(totalprice());
                    ((TextView) mPopupWindow.getContentView().findViewById(R.id.tv)).setText("Total= "+mytotal);

                } else {
                    ((TextView) mPopupWindow.getContentView().findViewById(R.id.tv)).setText("Sorry,Wrong Pass");

                }


            }
        });

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////


    public int totalprice() {
        String[] projection = {StudentEntry.COLUMN_STUDENT_COST};
        Cursor cursor =getContentResolver().query(StudentEntry.CONTENT_URI,projection,null,null,null);

       int total = 0;
        if (cursor.moveToFirst()) {
             do {
                int cost = cursor.getInt(cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_COST));
                total = total + cost;
                ContentValues values = new ContentValues();
                values.put(StudentEntry.COLUMN_STUDENT_COST, "");
                int rowsAffected = getContentResolver().update(StudentEntry.CONTENT_URI, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_student_failed),
                            LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_student_successful),
                            LENGTH_SHORT).show();
                }


            }while (cursor.moveToNext());
        }
        return total;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission ,Manifest.permission.READ_CONTACTS},
                    requestCode);
        }
        else {
           /* Toast.makeText(MainActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();*/
        }
    }
}





