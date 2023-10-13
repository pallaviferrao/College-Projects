package hk.ust.cse.comp107x.bookapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class removepage extends AppCompatActivity {
    private static final String TAG = "Remove page";
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<String> books;
    ArrayList<String> owners;

    private DatabaseReference removeReferenceUsers;
    private DatabaseReference removeReferenceBooks;
    private DatabaseReference removeReferenceData;
    String uid;
    int bookpositionint;
    String bookpositionString;
    //String bookId;

    TextView booknametextview, bookauthortextview, bookisbntextview, bookdescriptiontextview;
    private String downloadUrl, bookId, bookName, author, isbn, description;
    private ImageView imageview1,imageview2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removepage);
        booknametextview=(TextView)findViewById(R.id.booknametextview) ;
        bookauthortextview =(TextView)findViewById(R.id.bookauthortextview) ;
        bookisbntextview =(TextView)findViewById(R.id.bookisbntextview) ;
        bookdescriptiontextview =(TextView)findViewById(R.id.bookdescriptiontextview) ;
        imageview1=(ImageView)findViewById(R.id.backimage);
        imageview2=(ImageView)findViewById(R.id.smallimage);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();


       bookId = getIntent().getStringExtra("Book Id");
        bookName = getIntent().getStringExtra("Book name");
        downloadUrl = getIntent().getStringExtra("Download url");

        bookpositionString=getIntent().getStringExtra("position");
        bookpositionint=Integer.parseInt(bookpositionString);

        Log.d("Id of the book","  "+ bookpositionString);

        Picasso
                .with(this)
                .load(downloadUrl)
                .fit()
                .placeholder(R.drawable.load)
                .error(R.drawable.no_image)
                .into(imageview1, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("pic", "set ");
                    }

                    @Override
                    public void onError() {

                        Log.d("pic", "error ");
                    }
                });
        Picasso
                .with(this)
                .load(downloadUrl)
                .fit()
                .placeholder(R.drawable.load)
                .error(R.drawable.no_image)
                .into(imageview2, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("pic", "set ");
            }

            @Override
            public void onError() {

                Log.d("pic", "error ");
            }
        });



      /*  mDatabase.child("BOOKS").child(bookId).addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                 bookName = dataSnapshot.child("bookName").getValue(String.class);
                author = dataSnapshot.child("authorName").getValue(String.class);
                isbn = dataSnapshot.child("isbnNo").getValue(String.class);
                description = dataSnapshot.child("description").getValue(String.class);
                Log.d(TAG, "bookName : " + bookName);
                Log.d(TAG, "text view " + booknametextview.getText());
                booknametextview.append(bookName);
                bookauthortextview.append(author);
                bookisbntextview.append(isbn);
                bookdescriptiontextview.append(description);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mAuth.getCurrentUser() != null) ;

            }
        });


*/



        mDatabase.child("USERS").child(uid).child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                books= (ArrayList<String>) dataSnapshot.getValue();
                books.remove(bookpositionint);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("DATA").child(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                owners= (ArrayList<String>) dataSnapshot.getValue();
                int flag=0;
                for(int i=0;i<100;i++){
                    if(uid.equals(owners.get(i)));{
                        flag=i;
                        break;
                    }
                }
                Log.d("remove the owner",uid);
                owners.remove(flag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.removebookbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            removeReferenceUsers=mDatabase.child("USERS").child(uid).child("Books");

            removeReferenceUsers.setValue(books);

            removeReferenceBooks=mDatabase.child("BOOKS").child(bookId);
                removeReferenceBooks.removeValue();

                Log.d("name of thebook deleted",bookName);

            removeReferenceData=mDatabase.child("DATA").child(bookName).child(uid);
                removeReferenceData.setValue(owners);

            Log.d("removal of the book","book removed"+bookpositionString);
                Toast.makeText(removepage.this, "Book Removed Succesfully", Toast.LENGTH_SHORT).show();
               // account.myadapter.notifyDataSetChanged();
                Intent i=new Intent(removepage.this,LoginScreen.class);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // finish();
                startActivity(i);

            }
        });


    }
}
