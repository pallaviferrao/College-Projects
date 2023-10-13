package hk.ust.cse.comp107x.bookapp2;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Created by Padhavi on 27-06-2016.
 */
public class account extends Fragment {
    private String uid;
    private String currentUserName;
    private String currentUserEmail;
    private TextView userNameTextView, emailTextView;
    private FirebaseAuth auth;
    private static final String TAG = "Account";
    View myView;

    private DatabaseReference mDatabase;




    ArrayList<String> books;
    public static  booksadapter myadapter;
    private ListView Bookslist;
    private ArrayList<ImageItem> imageItemsArrayList;
    int flag = 0;
    FirebaseStorage storage;

   // @Override
    /*public void onResume() {
        super.onResume();
        Log.d(TAG,"on resume");
       *//* imageItemsArrayList =new ArrayList<>();
        try {
            Log.d(TAG, "adapter");
            //  if (myadapter == null)
            myadapter = new booksadapter(getActivity().getApplicationContext(), R.layout.removebookdetails, imageItemsArrayList);
            Bookslist.setAdapter(myadapter);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

       *//* fetchData();
        myadapter.notifyDataSetChanged();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ACCOUNT");

        myView = inflater.inflate(R.layout.account, container, false);
        userNameTextView = (TextView) myView.findViewById(R.id.Eemailid);
        emailTextView = (TextView) myView.findViewById(R.id.username);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        Log.d(TAG + " auth", uid);

        Log.d(TAG,"on create view");

       imageItemsArrayList =new ArrayList<>();
       uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
         final ListView Bookslist = (ListView)myView.findViewById(R.id.bookslist);

        try {
            Log.d(TAG, "adapter");
          //  if (myadapter == null)
                myadapter = new booksadapter(getActivity().getApplicationContext(), R.layout.removebookdetails, imageItemsArrayList);
            Bookslist.setAdapter(myadapter);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }


        mDatabase.child("USERS").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.child("name").getValue(String.class);
                currentUserEmail = dataSnapshot.child("email").getValue(String.class);
                Log.d(TAG + " name", currentUserName);
                userNameTextView.append(currentUserName);
                emailTextView.append(currentUserEmail);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (auth.getCurrentUser() != null)
                    Log.d(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
        Log.d(TAG,"jachcc");
        fetchData();

        /*try {
            Log.d(TAG, "adapter");
            if (myadapter == null)
                myadapter = new booksadapter(getActivity().getApplicationContext(), R.layout.removebookdetails, imageItemsArrayList);
            Bookslist.setAdapter(myadapter);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }*/

        Bookslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) Bookslist.getItemAtPosition(position);
               Log.d(TAG,"listner ");
                //Create intent
                Intent intent = new Intent(getActivity(), removepage.class);
                intent.putExtra("Book Id",item.getBookId());
                intent.putExtra("Book name",item.getTitle());
                intent.putExtra("Download url",item.getDownloadUrl());
               // intent.putExtra("Book Id Key",item.getBookId());
                String pos=Integer.toString(position);
                intent.putExtra("position",pos);
                startActivityForResult(intent,0);
            }
            public void onNothingSelected(AdapterView parentView)
            {
                Log.d(TAG,"listener nothing selcted");

            }

        });

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.addbookbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), addbook.class);
                startActivity(i);
            }
        });


        return myView;
    }

    private void fetchData()
    {
        mDatabase.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==2)
                {
                    Log.d(TAG,"no books added by user");
                }

                else
                {
                    books = (ArrayList<String>) dataSnapshot.child("Books").getValue();
                    Log.d(TAG,"bfore for loop");
                    for (int i = 0; i < books.size(); i++) {
                        //attach a listener at all book id's pbtained to get the required book details
                        mDatabase.child("BOOKS").child(books.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BookDetails obj = dataSnapshot.getValue(BookDetails.class);
                                if (obj != null) {
                                    ImageItem imageItemObject = new ImageItem(obj.getBookName(), obj.getDownloadUrl(), dataSnapshot.getKey());
                                    Log.d(TAG, "book id " + dataSnapshot.getKey());
                                    imageItemsArrayList.add(imageItemObject);
                                    Log.d(TAG,"name"+imageItemsArrayList.toString());
                                    myadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d(TAG, "listener at book id's " + databaseError.toString());
                            }
                        });


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.toString());
            }
        });
    }




}