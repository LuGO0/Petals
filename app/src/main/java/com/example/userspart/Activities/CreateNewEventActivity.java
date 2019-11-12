package com.example.userspart.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userspart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CreateNewEventActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;
    private EditText eventName,time,date,location;
    Button newEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_event_activity);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        eventName =(EditText)findViewById(R.id.event_name_value);
        time=(EditText)findViewById(R.id.time_value);
        date=(EditText)findViewById(R.id.date_value);
        location=(EditText)findViewById(R.id.location_value);

        newEventButton=findViewById(R.id.new_event_button2);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proposeNewEvent(eventName.getText().toString(),time.getText().toString(),date.getText().toString(),location.getText().toString());
                finish();
            }
        });
    }

    private void proposeNewEvent(String eventName, String time, String date, String location) {
        mDatabase.child("event").child(eventName).child("uid").setValue(mFirebaseUser.getUid());
        mDatabase.child("event").child(eventName).child("eventName").setValue(eventName);
        mDatabase.child("event").child(eventName).child("time").setValue(time);
        mDatabase.child("event").child(eventName).child("date").setValue(date);
        mDatabase.child("event").child(eventName).child("location").setValue(location);
        Toast.makeText(CreateNewEventActivity.this, "DONE", Toast.LENGTH_SHORT).show();

        //mDatabase.push().setValue("testing");
    }
}
