package com.licoforen.a6interactingwithapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    static final int SECOND_ACTIVITY_REQUEST = 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // *********** Explicit
        Intent activityIntent = new Intent(this, SecondActivity.class);
        activityIntent.putExtra("com.licoforen.a6interactingwithapps.firstnum", 5);
        activityIntent.putExtra("com.licoforen.a6interactingwithapps.secondnum", 7);
        startActivityForResult(activityIntent, SECOND_ACTIVITY_REQUEST);



        // *********** Implicit
        //Caution: If you invoke an intent and there is no app available on the device that can handle the intent, your app will crash.
        // Verification
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.android.com"));
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;
        //if(isIntentSafe) startActivity(intent);


        // Call
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        //startActivity(callIntent);


        // View map
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        //startActivity(mapIntent);


        // Web page
        Uri webpage = Uri.parse("http://www.android.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        //startActivity(webIntent);


        // Email with attachment
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        //startActivity(emailIntent);


        // Calendar event
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2012, 0, 19, 7, 30);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2012, 0, 19, 10, 30);
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
            calendarIntent.putExtra(CalendarContract.Events.TITLE, "Ninja class");
            calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Secret dojo");
            //startActivity(calendarIntent);
        }




        // Explicit app chooser (when user should select each time which app to use)
        //Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, "Share via");

        //Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivity(chooser);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SECOND_ACTIVITY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int num = data.getIntExtra("com.licoforen.a6interactingwithapps.mynumber", 0);
                Toast.makeText(this, "Success! Number: " + Integer.toString(num), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
