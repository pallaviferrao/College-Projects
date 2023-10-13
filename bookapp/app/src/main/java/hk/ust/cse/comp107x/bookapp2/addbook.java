package hk.ust.cse.comp107x.bookapp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Padhavi on 01-07-2016.
 */
public class addbook extends AppCompatActivity {

    private Button uploadPic, saveBook;
    private String userChoosenTask;
    private int REQUEST_CAMERA=2;
    private int SELECT_FILE=1;
    private ImageView ivImage;
    private int flag;
    private static final String TAG="AddBook";
    private EditText bookNameEditText, authorNameEditText, isbnNoEditText, descriptionEditText;
    private String bookName, authorName, isbnNum, description,owner;
    private DatabaseReference mDatabase;
    private Uri downloadUrl;
    private FirebaseStorage storage;
    private Uri fileUri=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        saveBook = (Button) findViewById(R.id.savebookbutton);
        uploadPic = (Button) findViewById(R.id.btnSelectPhoto);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        bookNameEditText = (EditText) findViewById(R.id.NameOfTheBookEdittext);
        authorNameEditText = (EditText) findViewById(R.id.AuthorNameEdittext);
        isbnNoEditText = (EditText) findViewById(R.id.IsbnNoEdittext);
        descriptionEditText = (EditText) findViewById(R.id.DescriptionEdittext);
        flag = 0;
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookName = bookNameEditText.getText().toString();
                authorName = authorNameEditText.getText().toString();
                isbnNum = isbnNoEditText.getText().toString();
                description = descriptionEditText.getText().toString();

                if (!validate())
                    return;

                storeImageToFirebase(fileUri);

            }
        });



        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });




    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(addbook.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(addbook.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Log.d(TAG,"permission denied");
                    Toast.makeText(addbook.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Bitmap bm=onSelectFromGalleryResult(data);
                Log.d(TAG,"gallery");
                ivImage.setImageBitmap(bm);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                Log.d(TAG,"image");
            }

        }
    }

    @SuppressWarnings("deprecation")
    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            Log.d(TAG,"data not null");
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                getUri(bm);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flag=1;
        return bm;
    }

    private void onCaptureImageResult(Intent data) {

        // Bitmap bitmap = ivImage.getDrawingCache();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ivImage.setImageBitmap(thumbnail);
        flag=1;
        getUri(thumbnail);
    }

    private boolean validate()
    {
        boolean valid=true;
        if(TextUtils.isEmpty(bookName))
        {
            bookNameEditText.setError("Required");
            valid=false;
        }
        else {
            bookNameEditText.setError(null);
        }

        if(TextUtils.isEmpty(authorName))
        {
            authorNameEditText.setError("Required");
            valid=false;
        }
        else {
            authorNameEditText.setError(null);
        }

        if(TextUtils.isEmpty(isbnNum))
        {
            isbnNoEditText.setError("Required");
            valid=false;
        }
        else {
            isbnNoEditText.setError(null);
        }

        if(TextUtils.isEmpty(description))
        {
            descriptionEditText.setError("Required");
            valid=false;
        }
        else {
            descriptionEditText.setError(null);
        }
        if(flag==0)
        {
            Toast.makeText(addbook.this, "Image required", Toast.LENGTH_SHORT).show();
            valid=false;
        }

        return valid;
    }

    private void getUri(Bitmap bitmap)
    {
        ivImage.setDrawingCacheEnabled(true);
        ivImage.buildDrawingCache();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        Log.d(TAG,"dest"+ destination);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileUri=Uri.fromFile(destination);

    }

    private void storeImageToFirebase(Uri fileuri) {
        storage = FirebaseStorage.getInstance();
        String url="gs://bookapp-ad81f.appspot.com";

        Log.d(TAG,"uplpoading");
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        StorageReference imagesRef = storageRef.child("photos").child(fileuri.getLastPathSegment());

        UploadTask uploadTask = imagesRef.putFile(fileuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG,"upload failed "+exception);
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG,"upload successful");
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                downloadUrl = taskSnapshot.getDownloadUrl();
                if(downloadUrl!=null)
                {
                    Log.d(TAG,"url success " +downloadUrl.toString() );

                    mDatabase= FirebaseDatabase.getInstance().getReference();
                    final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    BookDetails newBook=new BookDetails(bookName, authorName, isbnNum, description,downloadUrl.toString(),uid);
                    final String bookId= mDatabase.child("BOOKS").push().getKey();
                    mDatabase.child("BOOKS").child(bookId).setValue(newBook);
                    //notify adapter
                    loginpage.gridAdapter.notifyDataSetChanged();
                    Log.d(TAG+" bookId" ,bookId);
                    final String uid2=uid;

                    mDatabase.child("USERS").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()==2)
                            {
                                ArrayList<String> bookList=new ArrayList<String>();
                                bookList.add(bookId);
                                mDatabase.child("USERS").child(uid2).child("Books").setValue(bookList);
                                Log.d(TAG,"books node added");
                            }

                            else
                            {
                                ArrayList<String> bookList=(ArrayList<String>)dataSnapshot.child("Books").getValue();
                                bookList.add(bookId);
                                mDatabase.child("USERS").child(uid2).child("Books").setValue(bookList);
                                Log.d(TAG,"books node updated");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //  mDatabase.child("USERS").child(uid).child("Books").push().setValue(bookId);

                    /*attach listener to database to check if "DATA" node exists or not
                        It creates it and adds the data if it doesn't exist */
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG,"xxx count"+dataSnapshot.getChildrenCount());
                          /*  If datasnapshot has only 2 children i.e. "USERS" and "BOOKS" then create a
                                    new child "DATA" which stores the hashmap*/
                            if(dataSnapshot.getChildrenCount()==2)
                            {
                                Log.d(TAG,"no data child");
                                ArrayList<String> x=new ArrayList<>();
                                x.add(uid);
                                Log.d(TAG,"new owner array: "+x.toString());
                                mDatabase.child("DATA").child(bookName).setValue(x);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("DATA").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int flag=0;
                            for(DataSnapshot mdataSnapshot :dataSnapshot.getChildren())
                            {
                                if(mdataSnapshot.getKey().equals(bookName))
                                {
                                    Log.d(TAG,"book already exists");
                                    ArrayList<String> x=(ArrayList<String>)mdataSnapshot.getValue();
                                    // x.add(bookId);
                                    x.add(uid);
                                    Log.d(TAG,"owner array : "+x.toString());
                                    mDatabase.child("DATA").child(bookName).setValue(x);
                                    flag=1;  //make flag 1 if book exists
                                }

                            }
                            if(flag==0)  //book doesn't exist
                            {
                                {
                                    Log.d(TAG,"new book addded ");
                                    ArrayList<String> x=new ArrayList<String>();
                                    // x.add(bookId);
                                    x.add(uid);
                                    Log.d(TAG,"new owner array: "+x.toString());
                                    mDatabase.child("DATA").child(bookName).setValue(x);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   // finish();

                    Intent i=new Intent(addbook.this,LoginScreen.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Toast.makeText(addbook.this, "book added successfully",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


