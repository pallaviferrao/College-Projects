package hk.ust.cse.comp107x.bookapp2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectBook extends ActionBarActivity {
    private FirebaseAuth auth;
    private static final String TAG = "Select Book";
    private DatabaseReference mDatabase;
    private String uid;
    private static ArrayList<String> ownerList;
    private static ArrayList<String> x;
    private TextView bookNameTextView, bookauthor, bookisbn, bookdescription;
    private String downloadUrl, bookId, bookName, author, isbn, description;
    private ImageView imageView;
    public static lenderlistviewadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_book);

        x=new ArrayList<>();

            final ListView listview = (ListView) findViewById(R.id.lenderlist);



            //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            imageView = (ImageView) findViewById(R.id.imageView);
            bookNameTextView = (TextView) findViewById(R.id.bookname);
            bookauthor = (TextView) findViewById(R.id.bookauthor);
            bookisbn = (TextView) findViewById(R.id.bookisbn);
            bookdescription = (TextView) findViewById(R.id.bookdescription);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            auth = FirebaseAuth.getInstance();
            uid = auth.getCurrentUser().getUid();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            bookId = getIntent().getStringExtra("Book Id");
            bookName = getIntent().getStringExtra("Book name");
            downloadUrl = getIntent().getStringExtra("Download url");
            Log.d(TAG, "url " + downloadUrl);
            Log.d(TAG, bookId);

            Picasso
                    .with(this)
                    .load(downloadUrl)
                    .fit()
                    .placeholder(R.drawable.load)
                    .error(R.drawable.no_image)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("pic", "set ");
                        }

                        @Override
                        public void onError() {

                            Log.d("pic", "error ");
                        }
                    });

            mDatabase.child("BOOKS").child(bookId).addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    //  bookName = dataSnapshot.child("bookName").getValue(String.class);
                    author = dataSnapshot.child("authorName").getValue(String.class);
                    isbn = dataSnapshot.child("isbnNo").getValue(String.class);
                    description = dataSnapshot.child("description").getValue(String.class);
                    Log.d(TAG, "bookName : " + bookName);
                    Log.d(TAG, "text view " + bookNameTextView.getText());
                    bookNameTextView.append(bookName);
                    bookauthor.append(author);
                    bookisbn.append(isbn);
                    bookdescription.append(description);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (auth.getCurrentUser() != null) ;
                    // Log.d(LOG_TAG, "getUser:onCancelled", databaseError.toException());
                }
            });


            Query query = mDatabase.child("DATA").orderByKey().equalTo(bookName);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "query count " + dataSnapshot.getChildrenCount());
                    Log.d(TAG, "query key " + dataSnapshot.getKey());
                    for (DataSnapshot mdataSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "child " + mdataSnapshot.getKey());
                        ownerList=(ArrayList<String>)mdataSnapshot.getValue();
                        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectBook.this.getApplicationContext(),
                                android.R.layout.lenderlistview, android.R.id.text1, values);*/
                        Log.d(TAG,"ownwer list " + ownerList.toString());
                    }

                        mDatabase.child("USERS").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(int i=0;i<ownerList.size();i++)
                                {
                                    Log.d(TAG,"xx"+ dataSnapshot.child(ownerList.get(i)).child("name").getValue());
                                    x.add(dataSnapshot.child(ownerList.get(i)).child("name").getValue().toString());
                                }
                                Log.d(TAG,"x list "+x.toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//                         adapter = new lenderlistviewadapter(SelectBook.this.getApplicationContext(), R.layout.lenderlistview,ownerList);
                    adapter = new lenderlistviewadapter(SelectBook.this.getApplicationContext(), R.layout.lenderlistview,x);
                        Log.d(TAG,"ownwer list 2 " + ownerList.toString());
                    listview.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG,databaseError.toString());
                }
            });

        }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
