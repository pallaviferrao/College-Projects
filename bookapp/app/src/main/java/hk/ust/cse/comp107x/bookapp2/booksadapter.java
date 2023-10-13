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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


    public class booksadapter extends ArrayAdapter {
        //  static Bitmap b;
        ArrayList<String> bitmaps;
        // ArrayList<String> resource;
        LayoutInflater myinflater;
        Context context;
        private int layoutResourceId;
        private ArrayList<ImageItem> imageItems;
        private static final String TAG = "remove book adapter";

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            Log.d(TAG," books count "+imageItems.size());
            return imageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return imageItems.get(position);
        }

        public booksadapter(Context context, int layoutResourceId, ArrayList<ImageItem> imageItems) {
            super(context, layoutResourceId, imageItems);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.imageItems = imageItems;
            myinflater = LayoutInflater.from(context);
            Log.d(TAG,"constructor");
            Log.d(TAG,"image items "+imageItems.toString());

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*final ViewHolder holder;*/
            View rowView = convertView;
            ViewHolder holder = null;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.removebookdetails, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) rowView.findViewById(R.id.Removeimage);
                holder.imageTitle = (TextView) rowView.findViewById(R.id.name);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            ImageItem item = (ImageItem) imageItems.get(position);
           /* downloadurl(holder.imageView, item.getDownloadUrl());
            holder.imageTitle.setText(item.getTitle());*/
            Picasso
                    .with(context)
                    .load(item.getDownloadUrl())
                    .fit()
                    .placeholder(R.drawable.load)
                    .error(R.drawable.no_image)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("pic", "set ");
                        }

                        @Override
                        public void onError() {

                            Log.d("pic", "error ");
                        }
                    });
            Log.d(TAG, "book name " + item.getTitle());
            holder.imageTitle.setText(item.getTitle());
            return rowView;
        }

        private class ViewHolder {
            public ImageView imageView;
            public TextView imageTitle;
        }


    }
       /* public void downloadurl(final ImageView imageView, String url) {

            Picasso.with(context).load(url)
                    .placeholder(R.drawable.image3)
                    .error(R.drawable.no_image)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageView.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

        }
    }*/


