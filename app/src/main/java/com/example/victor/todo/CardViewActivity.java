package com.example.victor.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class CardViewActivity extends AppCompatActivity {

    //Reference variables here
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";


    //Access firebase
    private Firebase mRef, mTry;

    private String mUserId;
    private  String itemsUrl;
    private String myEmailList;
    private String xx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        mTry = new Firebase(Constants.TRY_URL);
        mTry.keepSynced(true);



//    @Override
//    protected void onStart() {
//        super.onStart();

        mRef = new Firebase(Constants.FIREBASE_URL);
        mRef.keepSynced(true);



        if (mRef.getAuth() == null) {
            loadLoginView();
        }
        //=======Add code to add items into firebase and return the items to be displays in the listview
        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {

        }


        //emailUrl= Constants.FIREBASE_URL + "/users/" + mUserId + "/email";
        myEmailList = Constants.FIREBASE_URL + "/myList";
        itemsUrl = Constants.FIREBASE_URL + "/users/" + mUserId + "/items";

        //Initializes references here
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapt er = new MyRecyclerViewAdapter(getDataSet());
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        mTry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                xx =  snapshot.getValue().toString(); //gets the specific data when it is changed
                //xx =  snapshot.getValue().toString(); //gets the specific data when it is changed

            }
            @Override public void onCancelled(FirebaseError error) { }
        });
/*
        // ===========Use Firebase to populate the list.
        new Firebase(itemsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //mAdapter.add((String) dataSnapshot.child("title").getValue());
                        String j =  dataSnapshot.child("title").getValue().toString();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String) dataSnapshot.child("title").getValue());
                        String dd = dataSnapshot.child("title").getValue().toString();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
*/
        // ===========Use Firebase to populate the list.

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }



    //Class to hold promary data here

    //remove final from results
    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        DataObject objz = new DataObject("First Primary ",
                "First Secondary ");
        for (int index = 0; index < 20; index++) {

            DataObject obj = new DataObject(xx +" " + index,
                    "Secondary " + index);


//            DataObject obj = new DataObject("Some Primary Text " + index,
//                    "Secondary " + index);
            results.add(index, obj);
        }


        return results;
    }


    //Rebouncing to login
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
