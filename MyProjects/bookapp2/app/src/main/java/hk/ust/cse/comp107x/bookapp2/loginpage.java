package hk.ust.cse.comp107x.bookapp2;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Padhavi on 27-06-2016.
 */
public class loginpage extends Fragment {

   private GridView gridView;
    public static gridviewadapter gridAdapter;
    public static Bitmap image;
    //private ActionBarActivity x;
    private static final String TAG="Books";
    private static int count;
    private HashSet<String> bookHashSet;
    private ArrayList<String> urlArrayList;
    private ArrayList<Bitmap> bitmapArrayList;
    private ArrayList<ImageItem> imageItemsArrayList;
   // private static int flag;
    private ProgressBar mProgressBar;
    private DatabaseReference database;

    View myView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database= FirebaseDatabase.getInstance().getReference();
      //  storage= FirebaseStorage.getInstance().getReference();
        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG,uid);

        bookHashSet = new HashSet<>();
        urlArrayList=new ArrayList<>();
        bitmapArrayList=new ArrayList<>();
        imageItemsArrayList =new ArrayList<>();
       // final int[] i={0};
        //flag=0;
        count=0;

        database.child("BOOKS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot mdataSnapshot :dataSnapshot.getChildren())
                {
                    String bookName=mdataSnapshot.child("bookName").getValue().toString();
                    bookHashSet.add(bookName.toLowerCase());
//                    bookHashSet.add(mdataSnapshot.child("bookName").getValue().toString().toLowerCase());
                    Log.d(TAG,bookHashSet.toString());

                    if(bookHashSet.size()!=count)
                    {
                        if(!mdataSnapshot.child("owner").getValue().toString().equals(uid)) {
                            Log.d(TAG,"added book name " +bookName);
                            String downloadUrl=mdataSnapshot.child("downloadUrl").getValue().toString();
                            String bookId=mdataSnapshot.getKey();
                            Log.d(TAG,"book key "+bookId);
                            ImageItem imageItem=new ImageItem(bookName,downloadUrl,bookId);
                            imageItemsArrayList.add(imageItem);
                          //  urlArrayList.add(mdataSnapshot.child("downloadUrl").getValue().toString());
                            /*downloadImageTask dwnld=new downloadImageTask();
                            dwnld.execute(urlArrayList.get(i[0]));
                            i[0]++;*/
                        }
                        count++;
                        Log.d(TAG,"count "+Integer.toString(count));
                      //  Log.d(TAG,urlArrayList.toString());
                        Log.d(TAG," image iems count "+ imageItemsArrayList.size());

                    }

                }
                mProgressBar.setVisibility(View.GONE);
               // Log.d(TAG," bitmap array "+bitmapArrayList.toString());
                Log.d(TAG,"pass data called");
                passData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());


            }
        });
    }


    private void passData()
    {
        try {
           // if (gridAdapter == null)
                gridAdapter = new gridviewadapter(this.getActivity().getApplicationContext(), R.layout.grid_item_layout, imageItemsArrayList);
            gridView.setAdapter(gridAdapter);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            Log.d(TAG,"error in try  catch "+e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("LOGIN PAGE");
        myView = inflater.inflate(R.layout.loginpage, container, false);
        gridView = (GridView) myView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) myView.findViewById(R.id.progressBar);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(getActivity(), SelectBook.class);
                    intent.putExtra("Book Id",item.getBookId());
                    intent.putExtra("Book name",item.getTitle());
                    intent.putExtra("Download url",item.getDownloadUrl());
              //  intent.putExtra("title", item.getTitle());
                // intent.putExtra("imageView", item.getImage());
               // imageView = item.getImage();
                startActivity(intent);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
       // mProgressBar.setVisibility(View.GONE);
        return myView;
    }
}
