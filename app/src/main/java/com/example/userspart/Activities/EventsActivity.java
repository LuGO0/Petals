package com.example.userspart.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.userspart.CustomTypes.Event;
import com.example.userspart.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.userspart.Adapters.EventAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRootRef;
    private DatabaseReference mDatabaseEventsRef;
    private ChildEventListener mChildEventListener;
    private FirebaseUser mFirebaseUser;
    private FloatingActionButton mProposeEvent;
    private EventAdapter mEventAdapter;
    private ListView mEventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event2);

        mDatabaseRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseEventsRef=mDatabaseRootRef.child("event");

        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mEventListView=(ListView)findViewById(R.id.event_list_view);



        //setting up the ArrayLIst for the lisTView
        List<Event> eventsList = new ArrayList<>();
        mEventAdapter = new EventAdapter(this, R.layout.item_event, eventsList);
        mEventListView.setAdapter(mEventAdapter);

        attachDatabaseReadListener();

        mProposeEvent=(FloatingActionButton) findViewById(R.id.new_event_button);
        mProposeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //..open a new activity and get data for pushing
                //make an Intent
                Intent intent=new Intent(EventsActivity.this, CreateNewEventActivity.class);
                startActivity(intent);
            }
        });

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    mEventAdapter.add(event);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mDatabaseEventsRef.addChildEventListener(mChildEventListener);
        }
    }


}
