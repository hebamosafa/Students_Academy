package com.example.mohamedelwarraky.students;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedelwarraky.students.data.StudentContract;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.apache.poi.hpsf.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class graph extends AppCompatActivity  {
    public static String strSeparator = ",";
    Uri mCurrentStudentUri;
    String mPath;
    String[] inputTel;
    String parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        int sessionId= getIntent().getIntExtra("EXTRA_SESSION_ID",0);



        String[]pro={
                StudentContract.StudentEntry._ID,
                StudentContract.StudentEntry.COLUMN_STUDENT_DEGREE,
                StudentContract.StudentEntry.COLUMN_STUDENT_TEL
        };

        Cursor cursor =getContentResolver().query(StudentContract.StudentEntry.CONTENT_URI,pro,null,null,null);
      if(cursor.moveToFirst()) {
          do {
               int ids=cursor.getColumnIndex(StudentContract.StudentEntry._ID);
               int myid=cursor.getInt(ids);
               int degreeColumnIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_STUDENT_DEGREE);
              int telColumnIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_STUDENT_TEL);
               String degree = cursor.getString(degreeColumnIndex);
               String tel=cursor.getString(telColumnIndex);
               String[] inputDegree = convertStringToArray(degree);
               inputTel = convertStringToArray(tel);

               int i=0;
               if(myid==sessionId)
               {     parent='2'+inputTel[1].trim();

                   List<DataPoint> dataPoints = new ArrayList<DataPoint>();
                   do {
                       dataPoints.add(
                               new DataPoint(i+1, Double.parseDouble(inputDegree[i])));
                       i++;

                   } while (i<inputDegree.length);

                   LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                           dataPoints.toArray(new DataPoint[0]));
                   series.setColor(R.color.magnitude5);
                   series.setDrawDataPoints(true);
                   series.setDataPointsRadius(10);
                   series.setThickness(8);




                   graph.getViewport().setScalable(true);
                   graph.getViewport().setScalableY(true);
                  graph.getViewport().scrollToEnd();

                   graph.addSeries(series);
                   graph.getViewport().setXAxisBoundsManual(true);
                   graph.getViewport().setMinX(1);
                   graph.getViewport().setMaxX(inputDegree.length);

                   graph.getViewport().setYAxisBoundsManual(true);
                   graph.getViewport().setMinY(0);
                   graph.getViewport().setMaxY(20);


               }




           } while (cursor.moveToNext());
       }


       }


    public static String[] convertStringToArray(String str) {

        String[] arr = str.split(strSeparator);
        return arr;
    }
    private void screen(){
      Date  now = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            mPath = Environment.getExternalStorageDirectory().toString() + "/" + "MATH_ACADEMY" + ".jpeg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }



    }
   // @SuppressLint("NewApi")
    private void openScreenshot(File imageFile) {
        String formattedNumber = "https://api.whatsapp.com/send?phone=" + parent;
        try {

            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Math academy\n"+"Mr.Mohamed Amin");
            whatsappIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, imageFile);
// generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open

            whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
            whatsappIntent.setType("image/jpeg");
//Directly send to specific mobile number...
            String smsNumber = parent;//Number without with country code and without '+' prifix
            whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix

            if (whatsappIntent.resolveActivity(getPackageManager()) == null) {
                return;
            }

            startActivity(whatsappIntent);
        }
            catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_whats:

                screen();

                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}