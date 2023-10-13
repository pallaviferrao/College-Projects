package hk.ust.cse.comp107x.bookapp2;
/**
 * Created by Padhavi on 12-07-2016.
 */


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class lenderlistviewadapter extends ArrayAdapter<String> {
    private  Context context;
    private  ArrayList<String> values;
    ArrayList<String> x;
    private FirebaseAuth auth;
    private String ownerName;
    private String text;
    private TextView textView;
    private int layoutResourceId;
    public static final String TAG = "lender";
    public lenderlistviewadapter(Context context, int layoutResourceId, ArrayList<String> values) {
        super(context, R.layout.lenderlistview, values);
        this.context = context;
        this.values = values;
        this.layoutResourceId=layoutResourceId;
        Log.d(TAG,"list "+values.toString());
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

       // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         View rowView = convertView;
       // ViewHolder holder=null;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater inflater = ((Application) context).getLayoutInflater();
            rowView = inflater.inflate(layoutResourceId, parent, false);
         //   holder = new ViewHolder();
          //  holder.ownerNameTextView = (TextView) rowView.findViewById(R.id.lendertext);
           // rowView.setTag(holder);
        }/* else {
            holder = (ViewHolder) rowView.getTag();
        }*/
        /*if(rowView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater.inflate(R.layout.lenderlistview, parent, false);
        }*/

             textView = (TextView) rowView.findViewById(R.id.lendertext);
        Log.d(TAG,"name " +values.get(position));
            textView.setText(values.get(position));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
       /* mDatabase.child("USERS").child(values.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"owner id : "+values.get(position));
                Log.d(TAG,"owner name "+ dataSnapshot.child("name").getValue());
                ownerName=dataSnapshot.child("name").getValue().toString();
                Log.d(TAG,"www "+ ownerName);
//                holder.ownerNameTextView.setText( dataSnapshot.child("name").getValue().toString());
                textView.setText(ownerName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.toString());
            }
        });*/

        return rowView;
    }

   /* static class ViewHolder {
        TextView ownerNameTextView;

    }*/
}

