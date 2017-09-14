package hk.ust.cse.comp107x.bookapp2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Padhavi on 29-06-2016.
 */

public class gridviewadapter extends ArrayAdapter {
    private int layoutResourceId;
    private final Context context;
    private  ArrayList<ImageItem> imageItems = new ArrayList<>();
    private LayoutInflater inflater;
    private static final String TAG="Grid View Adapter";


    public gridviewadapter(Context context, int layoutResourceId, ArrayList<ImageItem> imageItems) {
        super(context, layoutResourceId, imageItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
      //  this.bitmapArrayList =bitmapArrayList;
       // this.urls=urls;
        this.imageItems=imageItems;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder = null;

        if (rowView == null) {
            // LayoutInflater inflater = ((Application) context).getLayoutInflater();
            rowView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
              holder.imageTitle = (TextView) rowView.findViewById(R.id.text);
            holder.imageView = (ImageView) rowView.findViewById(R.id.image);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

       ImageItem item = (ImageItem) imageItems.get(position);
        // holder.imageTitle.setText(item.getTitle());
       /* Bitmap imageView=bitmapArrayList.get(position);
        holder.imageView.setImageBitmap(imageView);*/

        Picasso
                .with(context)
                .load(item.getDownloadUrl())
                .fit()
                .placeholder(R.drawable.image3)
                .error(R.drawable.no_image)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("pic","set ");
                    }

                    @Override
                    public void onError() {

                        Log.d("pic","error ");
                    }
                });
        Log.d(TAG, "book name "+item.getTitle());
        holder.imageTitle.setText(item.getTitle());
        return rowView;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView imageView;
    }
}

