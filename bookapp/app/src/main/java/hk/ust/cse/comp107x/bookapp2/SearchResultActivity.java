package hk.ust.cse.comp107x.bookapp2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Padhavi on 11-07-2016.
 */
public class SearchResultActivity extends ListActivity {

        private static final String TAG="Search Result";
        private DatabaseReference mDatabase;
        private String bookId,downloadUrl,bookName;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            handleIntent(getIntent());
        }

        public void onNewIntent(Intent intent) {
            setIntent(intent);
            handleIntent(intent);
        }

        public void onListItemClick(ListView l,
                                    View v, int position, long id) {
            // call detail activity for clicked entry
        }

        private void handleIntent(Intent intent) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query =
                        intent.getStringExtra(SearchManager.QUERY);
                doSearch(query);
            }
        }

        private void doSearch(final String queryStr) {
            // get a Cursor, prepare the ListAdapter
            // and set it
            final int[] i={0};
            Log.d(TAG,"seach string : " +queryStr);
            bookName=queryStr;
            mDatabase= FirebaseDatabase.getInstance().getReference();
            Query query=mDatabase.child("BOOKS").orderByChild("bookName").equalTo(queryStr);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG,"query " +dataSnapshot.getKey());
                    Log.d(TAG,"query value "+dataSnapshot.getValue());

                    for(DataSnapshot mdataSnapshot : dataSnapshot.getChildren())
                    {
                        downloadUrl=(mdataSnapshot.child("downloadUrl").getValue().toString());
                        bookId=mdataSnapshot.getKey();
                        Log.d(TAG,"book id "+ mdataSnapshot.getKey());
                        Log.d(TAG,"key "+ mdataSnapshot.child("downloadUrl").getValue());
                        i[0]++;
                        if(i[0]==dataSnapshot.getChildrenCount())
                        {
                            activityChange();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG,databaseError.toString());
                }
            });

        }

        public void activityChange()
        {
            Intent intent = new Intent(SearchResultActivity.this, SelectBook.class);
            intent.putExtra("Book Id",bookId);
            intent.putExtra("Book name",bookName);
            intent.putExtra("Download url",downloadUrl);
            Log.d(TAG,"intent");
            startActivity(intent);
        }
    }



