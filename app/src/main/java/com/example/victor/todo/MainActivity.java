package com.example.victor.todo;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

    final String TAG = "MAinActivity";

    private Firebase mRef, mTry, mEmail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private FirebaseAuth.AuthStateListener mAuthListener;

    //Variables to for userId and the items url
    private String mUserId;
    private String mEmailUrl;
    private String itemsUrl;
    private String myListUrl;
    private String myTryUrl;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Check Authentication
        //Here you create a Firebase reference pointing
        //to you app’s url, and then call getAuth() to check if the user is authenticated.

        //mTry = new Firebase(Constants.TRY_URL);
        //authListen();

//        if (mAuth.getCurrentUser() == null){
//            loadLoginView();
//
//        }
//
//        if (mRef.getAuth() == null) {
//            loadLoginView();
//        }






        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRef = new Firebase(Constants.FIREBASE_URL);
<<<<<<< HEAD

        mTry = new Firebase(Constants.TRY_URL);
        mEmail = new Firebase(Constants.EMAIL_URL);
        mEmail.keepSynced(true);
=======
        //set the username
>>>>>>> 50a0d42e644c3bf1fa8a76c7ae75a8ce2dce70f6



        try {
            mUserId = mRef.getAuth().getUid();
            Log.e(TAG, "========= GOT UserId Here=================");

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
            Log.e(TAG, "========= NO USER ID=================");
            loadLoginView();

        }


        /**
         if (mRef.getAuth() == null) {
         loadLoginView();
         } else {
         mUserId = mRef.getAuth().getUid();


         //=======Add code to add items into firebase and return the items to be displays in the listview
         try {
         mUserId = mRef.getAuth().getUid();
         } catch (Exception e) {
         loadLoginView();
         }
         }

         **/

        textView = (TextView) findViewById(R.id.user_name_final);

        itemsUrl = Constants.FIREBASE_URL + "/users/" + mUserId + "/items";
        myListUrl = Constants.FIREBASE_URL + "/myList";
        myTryUrl = Constants.TRY_URL;
        mEmailUrl = Constants.TRY_URL + "/users/" + mUserId + "/email";

        /*

        if (mAuth.getCurrentUser() == null) {
            loadLoginView();
        } else {
            mUserId = mAuth.getCurrentUser().getUid();


            // mEmail = new Firebase(Constants.EMAIL_URL);
            //mEmail = mRef.child(Constants.EMAIL_URL);


            // mEmail.child(mUserId).child("email").addValueEventListener(new ValueEventListener() {
            mRef.child("users").child(mUserId).child("email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String h = dataSnapshot.getValue().toString();
                    textView.setText("Welcome back " + h);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        */

        // mEmail.child(mUserId).child("email").addValueEventListener(new ValueEventListener() {
        mRef.child("users").child(mUserId).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String h = dataSnapshot.getValue().toString();
                textView.setText("Welcome back " + h);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set up ListView

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        // Add items via the Button and EditText at the bottom of the view.
        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Firebase(itemsUrl)
                        .push()
                        .child("title")
                        .setValue(text.getText().toString());
                text.setText("");
//                new Firebase(myListUrl)
//                        .push().child("title").setValue(text.getText().toString());
                new Firebase(myTryUrl).setValue(text.getText().toString());
                //myTryUrl.setValue(text.getText().toString()); //Write to the trything
            }
        });

        // Use Firebase to populate the list.
        new Firebase(itemsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add((String) dataSnapshot.child("title").getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String) dataSnapshot.child("title").getValue());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String whatToDelete = listView.getItemAtPosition(position).toString();

                DatabaseReference deletingReference = database.getReference().child("users").child(mUserId).child("items");

                deletingReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                        long noOfChildren = dataSnapshot.getChildrenCount();
                        // Toast.makeText(MainActivity.this, noOfChildren+"", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, noOfChildren+" CHILDREN FOUND");


                        for(com.google.firebase.database.DataSnapshot snap : dataSnapshot.getChildren()){
                            // snap gives you key values under the list of items recorded
                            // dataSnapshot.getChilren() helps loop the number of children under items
                            String onlyTheToDoList  = snap.child("title").getValue().toString();


                            if(onlyTheToDoList.equals(whatToDelete)){
                                Log.e(TAG, ">>>>> Snap value == >>>" + snap.getValue());
                                Log.e(TAG, ">>>> Specific item == >>>>" + onlyTheToDoList);

                                Log.e(TAG, " ++++++++++++++++++=+++++ ");
                                Log.e(TAG, "We found your item ------ "+ onlyTheToDoList);
                                snap.child("title").getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //// =================== TRY DELETING ITEMS HERE ===================
                //// ============******************************************======
                //// =================== TRY DELETING ITEMS HERE ===================
                /**
                new Firebase(itemsUrl)
                        .orderByChild("title")
                        .equalTo((String) listView.getItemAtPosition(position))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }

                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });

                **/
                Toast.makeText(MainActivity.this, listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //The logout button will redirect the user to the login activity
        if (id == R.id.action_logout) {
            // mRef.unauth();
            mAuth.signOut();
            loadLoginView();
        } else if (id == R.id.action_cards) {

            //Let this take you to your card view activity
            //mRef.unauth();
            mRef.getAuth();
            cardsView();
        }

        return super.onOptionsItemSelected(item);
    }

    //Create the method to load login activity here
    //This navigates to the Login view and clears the activity stack.
    //This prevents the user going back to the main activity when they press the Back button from the login view.
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void cardsView() {
        Intent intent = new Intent(this, CardViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void authListen(){

        Log.e(TAG, "authListen method");
        // ...
        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mUserId = user.getUid();
                    Log.e(TAG, "========= GOT UserId Here too =================>> " +mUserId );
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    loadLoginView();
                }
                // ...
            }
        };
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
       /*
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        */
    }

}
