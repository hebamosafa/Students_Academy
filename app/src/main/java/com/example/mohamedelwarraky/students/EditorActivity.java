package com.example.mohamedelwarraky.students;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.*;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedelwarraky.students.data.StudentContract;
import com.example.mohamedelwarraky.students.data.StudentContract.StudentEntry;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Allows user to create a new student or edit an existing one.
 */
public class   EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    int total;
    graph degreegraph;
    LinearLayout mLayout;
    int myid;
    String parent;
    String sendedd;
    String[] inputDegree;
    Date now;
    String mPath;
    GestureDetector gestureDetector;
    /**
     * Identifier for the pet data loader.
     */
    private static final int EXISTING_PET_LOADER = 0;

    /**
     * Content URI for the existing student (null if it's a new student).
     */
    private Uri mCurrentStudentUri;
int i=0;
    /**
     * EditText field to enter the student's name.
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the student's group.
     */
    private EditText mGroupEditText;

    /**
     * EditText field to enter the first student's degree.
     */
    private EditText mFirstDegreeEditText;

    /**
     * EditText field to enter the first student's cost.
     */
    private EditText mCostEditText;

    /**
     * EditText field to enter the second student's degree.
     */
    private EditText mSecondDegreeEditText;

    /**
     * EditText field to enter the third student's degree.
     */
    private EditText mThirdDegreeEditText;

    /**
     * EditText field to enter the fourth student's degree.
     */
    private EditText mFourthDegreeEditText;
    private EditText mcostEditText;


    /**
     * EditText field to enter the Phone number.
     */
    private EditText mTelEditText;

    /**
     * EditText field to enter the School.
     */
    private EditText mSchoolEditText;

    /**
     * Boolean flag that keeps track of whether the pet has been edited (true) or not (false)
     */
    private boolean mStudentHasChanged = false;

    /**
     * Number of created views
     */
    int numberOfView;
    int numberOfView2;
    CheckBox check;
    static int count=0;

    /**
     * To store created EditText.
     */
    ArrayList<EditText> DegreeEditText = new ArrayList<>();
    ArrayList<String> dayscheckbox = new ArrayList<>();
    ArrayList<CheckBox> dayscheckbox2 = new ArrayList<>();

    /**
     * To store created EditText.
     */
    ArrayList<EditText> TelEditText = new ArrayList<>();


    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mStudentHasChanged = true;
            return false;
        }
    };


    public EditorActivity() {
        gestureDetector = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // Button to add phone number view
        ImageView AddViewTel = findViewById(R.id.add_view_number);
        /**
         * When user press AddView, view will appear.
         *
         * View contains an EditText for phone number.
         */
        AddViewTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Layout that i will create my view into.
                 final LinearLayout mLayout = findViewById(R.id.tel_layout);
                // Create new EditText.
                final EditText newEditText = new EditText(EditorActivity.this);
                // Setup OnTouchListeners on the phone number field, so we can determine if the user
                // has touched or modified them. This will let us know if there are unsaved changes
                // or not, if the user tries to leave the editor without saving.
                newEditText.setOnTouchListener(mTouchListener);
                // Set params of the EditText.
                newEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                newEditText.setHint(R.string.hint_student_number);
                newEditText.setTextAppearance(EditorActivity.this, android.R.style.TextAppearance_Material_Medium);
                newEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                newEditText.setTextColor(Color.BLACK);
                // Add to ArrayList To store All Phone Number EditText.
                TelEditText.add(newEditText);
                // Add EditText to the layout.
                mLayout.addView(newEditText);
                ////////////////////////////////////
                newEditText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();

                        mLayout.removeView(v);
                        return true;
                    }

                });

            }
        });

                   ///////////////////////////////////////////////////////////////////////////////////////////////

        // Button to add degrees view
        ImageView AddViewDegree = findViewById(R.id.add_view_degree);


        numberOfView = 0;
        /**
         * When user press AddView, view will appear.
         *
         * View contains 4 EditTexts for degrees.
         */
        AddViewDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Layout that i will create my view into.
                LinearLayout mLayout = findViewById(R.id.degree_layout);
                // Create new horizontal Layout to add EditText.
                LinearLayout row = new LinearLayout(EditorActivity.this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                for (int j = numberOfView; j < numberOfView + 4; j++) {

                    // Create new EditText.
                    EditText EditTextTag = new EditText(EditorActivity.this);
                    // Setup OnTouchListeners on the Degrees fields, so we can determine if the user
                    // has touched or modified them. This will let us know if there are unsaved changes
                    // or not, if the user tries to leave the editor without saving.
                    EditTextTag.setOnTouchListener(mTouchListener);
                    // Set param of the 4 EditTexts.
                    EditTextTag.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    if (j == numberOfView + 3)
                        EditTextTag.setHint(R.string.hint_student_exam);
                    else
                        EditTextTag.setHint(R.string.hint_student_quiz);
                    EditTextTag.setId(j);
                    EditTextTag.setTextAppearance(EditorActivity.this, android.R.style.TextAppearance_Material_Small);
                    EditTextTag.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                    EditTextTag.setTextColor(Color.BLACK);
                    EditTextTag.setGravity(Gravity.CENTER);
                    // Add to ArrayList To store All Degrees EditText.
                    DegreeEditText.add(EditTextTag);
                    // Add EditText to the layout.
                    row.addView(EditTextTag);
                }
                // increase numberOfView to add more views.
                numberOfView = numberOfView + 4;
                // Add the new layout to mLayout.
                mLayout.addView(row);
            }
        });
        ImageView cost = findViewById(R.id.add_view_cost);
        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCostEditText.getVisibility() == View.VISIBLE) {
                    mCostEditText.setVisibility(View.INVISIBLE);
                } else {
                    mCostEditText.setVisibility(View.VISIBLE);
                }

            }
        });


        ImageView AddViewDays = findViewById(R.id.add_view_days);


        numberOfView2 = 0;
        /**
         * When user press AddView, view will appear.
         *
         * View contains 4 EditTexts for degrees.
         */ ///////////////////////////////////////////////////////////////////////////////////////////////////////
        AddViewDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Layout that i will create my view into.
                LinearLayout mLayout = findViewById(R.id.days_layout);
                // Create new horizontal Layout to add EditText.
                LinearLayout row = new LinearLayout(EditorActivity.this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                for (int j = numberOfView2; j < numberOfView2 + 4; j++) {

                    // Create new Check.
                    check = new CheckBox(EditorActivity.this);
                    dayscheckbox2.add(check);
                    // Setup OnTouchListeners on the Degrees fields, so we can determine if the user
                    // has touched or modified them. This will let us know if there are unsaved changes
                    // or not, if the user tries to leave the editor without saving.
                    check.setOnTouchListener(mTouchListener);
                    // Set param of the 4 EditTexts.
                    check.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));

                    check.setId(j);


                    // Add to ArrayList To store All Degrees EditText.

                    // Add EditText to the layout.
                    row.addView(check);


                }

                // increase numberOfView to add more views.
                numberOfView2 = numberOfView2 + 4;
                // Add the new layout to mLayout.
                mLayout.addView(row);
            }
        });


        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new student or editing an existing one.
        Intent intent = getIntent();
        mCurrentStudentUri = intent.getData();


        // If the intent DOES NOT contain a student content URI, then we know that we are
        // creating a new student.
        if (mCurrentStudentUri == null) {
            // This is a new pet, so change the app bar to say "Add a Student"
            setTitle(getString(R.string.editor_activity_title_new_student));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a student that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing student, so change app bar to say "Edit Student"
            setTitle(getString(R.string.editor_activity_title_edit_student));

            // Initialize a loader to read the student data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_student_name);
        mGroupEditText = findViewById(R.id.edit_student_group);
        mFirstDegreeEditText = findViewById(R.id.first_student_degree);
        mSecondDegreeEditText = findViewById(R.id.second_student_degree);
        mThirdDegreeEditText = findViewById(R.id.third_student_degree);
        mFourthDegreeEditText = findViewById(R.id.fourth_student_degree);
        mCostEditText = findViewById(R.id.edit_student_cost);


        // Store all Degree EditView that define in activity_editor.xml to ArrayList
        DegreeEditText.add(mFirstDegreeEditText);
        DegreeEditText.add(mSecondDegreeEditText);
        DegreeEditText.add(mThirdDegreeEditText);
        DegreeEditText.add(mFourthDegreeEditText);

        mTelEditText = findViewById(R.id.student_phone_number);

        // Store all phone number EditView that define in activity_editor.xml to ArrayList
        TelEditText.add(mTelEditText);
        mSchoolEditText = findViewById(R.id.edit_student_school);


        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mGroupEditText.setOnTouchListener(mTouchListener);
        mTelEditText.setOnTouchListener(mTouchListener);
        mCostEditText.setOnTouchListener(mTouchListener);

        for (int i = 0; i < TelEditText.size(); i++) {
            TelEditText.get(i).setOnTouchListener(mTouchListener);
        }

        for (int i = 0; i < DegreeEditText.size(); i++) {
            DegreeEditText.get(i).setOnTouchListener(mTouchListener);
        }

        mSchoolEditText.setOnTouchListener(mTouchListener);

    }


    /**
     * Get user input from editor and save new student into database.
     */
    private void saveStudent() {


        for (int i = 0; i < dayscheckbox2.size(); i++)
            if (dayscheckbox2.get(i).isChecked())
                dayscheckbox.add("Y");
            else
                dayscheckbox.add("N");

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();

        String groupString = mGroupEditText.getText().toString().trim();
        String costString;
        if (mCostEditText.getText().toString().trim().isEmpty())
            costString = "0";
        else
            costString = mCostEditText.getText().toString().trim();
        Float cost = Float.parseFloat(costString);

int flag=0;
boolean state=false;
        // String Array to store all data of degrees fields
        for(int k=0;k<DegreeEditText.size();k++){
            if(DegreeEditText.get(k).getText().toString().trim().isEmpty()){
               if(!state)
                   flag=0;
                 flag++;
                state=true;
                if(flag==4){
                    if(k==3){flag=0;}
else {
                        DegreeEditText.remove(k);
                        DegreeEditText.remove(k - 1);
                        DegreeEditText.remove(k - 2);
                        DegreeEditText.remove(k - 3);
                        flag = 0;
                        k=k-4;
                    }
                }
            }
            else{
                state=false;
            }

        }
        String[] DegreeString;

            DegreeString = new String[DegreeEditText.size()];

        for (int i = 0, j = 3; i < DegreeEditText.size(); i++) {

            // Get all data from Phone number fields and store them into Array.
            if (DegreeEditText.get(i).getText().toString().trim().isEmpty()) {
                DegreeString[i] = "0";
                continue;
            }

            if (i == j) {
                DegreeString[i] = DegreeEditText.get(i).getText().toString().trim() + "\n";

                j = j + 4;
            } else {
                DegreeString[i] = DegreeEditText.get(i).getText().toString().trim();
            }


        }
        for (int i = 0; i < TelEditText.size(); i++) {
            // Get all data from Phone number fields and store them into Arra


            if(TelEditText.get(i).getText().toString().isEmpty()){
                if(i==0)
                    continue;
                TelEditText.remove(i);
            i--;}
            }

        String[] TelString;
        // String Array to store all data of phone number fields
        if(TelEditText.size()==1){
         TelString = new String[TelEditText.size()+1];}
        else{
             TelString = new String[TelEditText.size()];
        }


        for (int i = 0; i < TelEditText.size(); i++) {
            // Get all data from Phone number fields and store them into Arra

                TelString[i] = TelEditText.get(i).getText().toString().trim() + "\n";

        }




        if(TelEditText.size()==1){
        TelString[TelEditText.size()] = "0";}
        if(TelEditText.get(0).getText().toString().trim().isEmpty()){
            TelString[0] = "0";}


        String[] dayssaved = new String[dayscheckbox.size()];

        for (int i = 0; i < dayscheckbox.size(); i++) {
            // Get all data from Phone number fields and store them into Array.
            for (int j = 0; j < dayscheckbox.size() / 4; j = j + 4)
                if (i == 3 + j) {
                    dayssaved[i] = dayscheckbox.get(i).trim() + "\n";

                } else
                    dayssaved[i] = dayscheckbox.get(i).trim();
        }

        String schoolString = mSchoolEditText.getText().toString().trim();

        // Check if this is supposed to be a new student
        // and check if all the fields in the editor are blank
        if (mCurrentStudentUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(groupString) &&
                TextUtils.isEmpty(DegreeString[0]) && TextUtils.isEmpty(DegreeString[1]) &&
                TextUtils.isEmpty(DegreeString[2]) && TextUtils.isEmpty(DegreeString[3]) &&
                TextUtils.isEmpty(TelString[0]) &&
                TextUtils.isEmpty(schoolString)) {
            // Since no fields were modified, we can return early without creating a new student.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }




        String DisplayName = "parent"+nameString;
        String MobileNumber="";
        if(!(TelString[1].equals(0))) {
            MobileNumber = TelString[1];


            if (!(contactExists(EditorActivity.this, MobileNumber))) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                //------------------------------------------------------ Names
                if ((DisplayName != null) & (MobileNumber != null)) {
                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(
                                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                    DisplayName).build());

                }

                //------------------------------------------------------ Mobile Number
                if (MobileNumber != null) {
                    ops.add(ContentProviderOperation.
                            newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build());

                }
                // Asking the Contact provider to create a new contact
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }

        // String Array to one String and separate it with ',' to store them into database.
        String Degree = convertArrayToString(DegreeString);
        String Tel = convertArrayToString(TelString);
        String days = convertArrayToString(dayssaved);

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(StudentEntry.COLUMN_STUDENT_NAME, nameString);
        values.put(StudentEntry.COLUMN_STUDENT_GROUP, groupString);
        values.put(StudentEntry.COLUMN_STUDENT_COST, cost);
        values.put(StudentEntry.COLUMN_STUDENT_SCHOOL, schoolString);
        values.put(StudentEntry.COLUMN_STUDENT_DEGREE, Degree);
        values.put(StudentEntry.COLUMN_STUDENT_DAYS, days);
        values.put(StudentEntry.COLUMN_STUDENT_TEL, Tel);

if(!(nameString.isEmpty())) {
    // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
    if (mCurrentStudentUri == null) {
        // This is a NEW pet, so insert a new pet into the provider,
        // returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(StudentEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_student_failed),
                    LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_student_successful),
                    LENGTH_SHORT).show();
        }
    } else {
        // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
        // and pass in the new ContentValues. Pass in null for the selection and selection args
        // because mCurrentPetUri will already identify the correct row in the database that
        // we want to modify.
        int rowsAffected = getContentResolver().update(mCurrentStudentUri, values, null, null);

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
    }
}
else Toast.makeText(this,"Data cann't be saved without entering student's name",
        Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentStudentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database

                if (ContextCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.WRITE_CONTACTS)
                        == PackageManager.PERMISSION_DENIED) {

                    // Requesting the permission
                    ActivityCompat.requestPermissions(EditorActivity.this,
                            new String[] {  Manifest.permission.WRITE_CONTACTS },
                            102);
                }
                else {
                    Toast.makeText(EditorActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                            .show();}
                saveStudent();



                finish();
                return true;
            case R.id.action_send:
                // Save pet to database
               sendtext();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);*/


                return true;
            case R.id.action_draw:
                // Pop up confirmation dialog for deletion
                graph();

                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mStudentHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mStudentHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteStudent();
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


    /**
     * Perform the deletion of the student in the database.
     */
    private void deleteStudent() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentStudentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentStudentUri, null, null);
            //((excel)  this.getApplication()).set_excel();

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_student_failed),
                        LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_student_successful),
                        LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                StudentEntry._ID,
                StudentEntry.COLUMN_STUDENT_NAME,
                StudentEntry.COLUMN_STUDENT_GROUP,
                StudentEntry.COLUMN_STUDENT_COST,
                StudentEntry.COLUMN_STUDENT_SCHOOL,
                StudentEntry.COLUMN_STUDENT_DEGREE,
                StudentEntry.COLUMN_STUDENT_DAYS,
                StudentEntry.COLUMN_STUDENT_TEL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentStudentUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
       if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_NAME);
            int groupColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_GROUP);
            int schoolColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_SCHOOL);
            int degreeColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_DEGREE);
            int telColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_TEL);
            int daysColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_DAYS);
            int ids = cursor.getColumnIndex(StudentContract.StudentEntry._ID);
            myid = cursor.getInt(ids);

            // Extract out the value from the Cursor for the given column index

            String name = cursor.getString(nameColumnIndex);
            String group = cursor.getString(groupColumnIndex);
            String school = cursor.getString(schoolColumnIndex);
            String degree = cursor.getString(degreeColumnIndex);
            String days = cursor.getString(daysColumnIndex);
            String tel = cursor.getString(telColumnIndex);
            Float cost = cursor.getFloat(cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_COST));
            String cost2 = String.valueOf(cost);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mGroupEditText.setText(group);
            mSchoolEditText.setText(school);
            mCostEditText.setText(cost2);


            Log.v("EditorActivity", "input degree " + degree);
            Log.v("EditorActivity", "Size EditText " + DegreeEditText.size());
            inputDegree = convertStringToArray(degree);

            final String[] inputTel = convertStringToArray(tel);

            LinearLayout mLayoutTel = null;
            int i;
            for (i = 0; i < inputTel.length - 1-count; i++) {

                 mLayoutTel = findViewById(R.id.tel_layout);
                EditText newEditText = new EditText(EditorActivity.this);
                newEditText.setOnTouchListener(mTouchListener);
                newEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                newEditText.setHint(R.string.hint_student_number);
                newEditText.setTextAppearance(EditorActivity.this, android.R.style.TextAppearance_Material_Medium);
                newEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                newEditText.setTextColor(Color.BLACK);
                TelEditText.add(newEditText);
               mLayoutTel.addView(newEditText);

            }
            parent = "+2" + inputTel[1];
            for (i = 0; i < inputTel.length-count; i++)
                if (inputTel.length > i) {

                        TelEditText.get(i).setText(inputTel[i]);
                        Log.v("EditorActivity", "input " + inputTel[i]);

                        final int finalI = i;
                        TelEditText.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+inputTel[finalI]));
                            startActivity(intent);
                                return true;
                                /* addAsContactConfirmed(EditorActivity.this);
                                  return true;*/
                            }
                        });


                    }





            LinearLayout mLayoutDegree = findViewById(R.id.degree_layout);
            numberOfView = 0;
            int numberOfRows;

            if ((double) inputDegree.length / 4.0 - inputDegree.length / 4 == 0)
                numberOfRows = inputDegree.length - 1;
            else
                numberOfRows = inputDegree.length;

            for (i = 0; i < numberOfRows / 4; i++) {
                LinearLayout row = new LinearLayout(EditorActivity.this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                for (int j = numberOfView; j < numberOfView + 4; j++) {

                    EditText EditTextTag = new EditText(EditorActivity.this);
                    EditTextTag.setOnTouchListener(mTouchListener);
                    EditTextTag.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    if (j == numberOfView + 4)
                        EditTextTag.setHint(R.string.hint_student_exam);
                    else
                        EditTextTag.setHint(R.string.hint_student_quiz);

                    EditTextTag.setTextAppearance(EditorActivity.this, android.R.style.TextAppearance_Small);
                    EditTextTag.setTextColor(Color.BLACK);
                    EditTextTag.setGravity(Gravity.CENTER);
                    EditTextTag.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
                    EditTextTag.setText("0");

                    DegreeEditText.add(EditTextTag);
                    row.addView(EditTextTag);
                }
                numberOfView = numberOfView + 4;
                mLayoutDegree.addView(row);
            }
            Log.v("EditorActivity", "input.length " + inputDegree.length);
            degreegraph = new graph();

            for (i = 0; i < inputDegree.length; i++)
                if (inputDegree.length > i) {
                    DegreeEditText.get(i).setText(inputDegree[i]);
                    if (!(inputDegree[i].trim().equals("0")))
                        sendedd = inputDegree[i];
                    DegreeEditText.get(i).setBackgroundResource(R.drawable.circle);

                    GradientDrawable magnitudeCircle = (GradientDrawable) DegreeEditText.get(i).getBackground();
                    int degreecolor = getMagnitudeColor(inputDegree[i]);
                    magnitudeCircle.setColor(degreecolor);

                    Log.v("EditorActivity", "input " + inputDegree[i]);
                } else
                    DegreeEditText.get(i).setText("");


            int number = 0;
            String[] inputdays = convertStringToArray(days);
            LinearLayout mLayoutdays = findViewById(R.id.days_layout);

            for (i = 0; i < inputdays.length / 4; i++) {
                LinearLayout row = new LinearLayout(EditorActivity.this);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                for (int j = number; j < number + 4; j++) {

                    CheckBox newcheck = new CheckBox(EditorActivity.this);
                    newcheck.setOnTouchListener(mTouchListener);
                    newcheck.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    newcheck.setId(j);
                    dayscheckbox2.add(newcheck);
                    if (inputdays[j].equals("Y" + "\n") || inputdays[j].equals("Y"))
                        newcheck.setChecked(true);
                    else if (inputdays[j].equals("N"))
                        newcheck.setChecked(false);
                    row.addView(newcheck);
                }
                number = number + 4;

                mLayoutdays.addView(row);

            }

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mGroupEditText.setText("");
        mSchoolEditText.setText("");
        for (int i = 0; i < DegreeEditText.size(); i++) {
            DegreeEditText.get(i).setText("");
        }
        for (int i = 0; i < TelEditText.size(); i++) {
            TelEditText.get(i).setText("");
        }

    }

    public static String strSeparator = ",";

    public static String convertArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);
        return arr;
    }



    private int getMagnitudeColor(String magnitude) {
        int magnitudeColorResourceId = 0;
        // int magnitudeFloor = (int) Math.floor(magnitude);
        Float M;

        M = Float.parseFloat(magnitude.trim());


        if (M >= 9) {
            magnitudeColorResourceId = R.color.magnitude4;
        } else if (M < 9 & M >= 7) {
            magnitudeColorResourceId = R.color.magnitude3;
        } else if (M < 7 & M > 5) {

            magnitudeColorResourceId = R.color.magnitude1;
        } else {
            magnitudeColorResourceId = R.color.magnitude5;
        }
        return ContextCompat.getColor(EditorActivity.this, magnitudeColorResourceId);
    }


    public void graph() {
        Intent intent = new Intent(getBaseContext(), graph.class);
        intent.putExtra("EXTRA_SESSION_ID", myid);
        startActivity(intent);


    }

   private void sendtext(){

       try {
           PackageManager packageManager = this.getPackageManager();
           Intent i = new Intent(Intent.ACTION_VIEW);

           String url = "https://api.whatsapp.com/send?phone=" + parent + "&text="
                   + URLEncoder.encode("Math academy\n"+"Mr.Mohamed Amin\n"
                   +"The degree of last quiz: " + sendedd, "UTF-8");
           i.setPackage("com.whatsapp");
           i.setData(Uri.parse(url));
           if (i.resolveActivity(packageManager) != null) {
               this.startActivity(i);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public boolean contactExists(Activity _activity, String number) {
        if (number != null) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
            Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }// contactExists
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    };
    public static void addAsContactConfirmed(final Context context) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, "bdod");
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "0111747");


        context.startActivity(intent);

    }

}








