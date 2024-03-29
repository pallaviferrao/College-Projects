package com.example.pallavi.kshamata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.games.leaderboard.LeaderboardEntity;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.contract.Face;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class analyze extends AppCompatActivity {


    private static final int REQUEST_SELECT_IMAGE = 0;

    // The button to select an image
    private Button mButtonSelectImage;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    private Bitmap mBitmap;

    // The edit to show status and result.
    private EditText mEditText;

    private VisionServiceClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

         // Flag to indicate which task is to be performed.



                if (client==null){
                    client = new VisionServiceRestClient(getString(R.string.subscription_key));
                }

                mButtonSelectImage = (Button)findViewById(R.id.buttonSelectImage);
                mEditText = (EditText)findViewById(R.id.editTextResult);
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_analyze, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

            public void doAnalyze() {
                Toast.makeText(this,"analysing",Toast.LENGTH_SHORT).show();
                mButtonSelectImage.setEnabled(false);
                mEditText.setText("Analyzing...");

                try {
                    new doRequest().execute();
                } catch (Exception e)
                {
                    mEditText.setText("Error encountered. Exception is: " + e.toString());
                }
            }

            // Called when the "Select Image" button is clicked.
            public void selectImage(View view) {
                mEditText.setText("");

                Intent intent;
                intent = new Intent(analyze.this, select_image_activity.class);
                startActivityForResult(intent, REQUEST_SELECT_IMAGE);
            }

            // Called when image selection is done.
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                Log.d("AnalyzeActivity", "onActivityResult");
                Toast.makeText(this,"working",Toast.LENGTH_SHORT).show();
                switch (requestCode) {
                    case REQUEST_SELECT_IMAGE:
                        if(resultCode == RESULT_OK) {
                            // If image is selected successfully, set the image URI and bitmap.
                            mImageUri = data.getData();

                            mBitmap = image_helper.loadSizeLimitedBitmapFromUri(
                                    mImageUri, getContentResolver());
                            if (mBitmap != null) {
                                // Show the image on screen.
                                ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                                imageView.setImageBitmap(mBitmap);

                                // Add detection log.
                                Log.d("AnalyzeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                                        + "x" + mBitmap.getHeight());

                                doAnalyze();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }


            private String process() throws VisionServiceException, IOException {
                Gson gson = new Gson();
                String[] features = {"ImageType", "Color", "Faces", "Adult", "Categories"};
                String[] details = {};

                // Put the image into an input stream for detection.
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

                AnalysisResult v = this.client.analyzeImage(inputStream, features, details);

                String result = gson.toJson(v);
                Log.d("result", result);


                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

                return result;
            }

            private class doRequest extends AsyncTask<String, String, String> {
                // Store error message
                private Exception e = null;

                public doRequest() {
                }

                @Override
                protected String doInBackground(String... args) {
                    try {
                        return process();
                    } catch (Exception e) {
                        this.e = e;    // Store error
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String data) {
                    super.onPostExecute(data);
                    // Display based on error existence

                    mEditText.setText("");
                    if (e != null) {
                        mEditText.setText("Error: " + e.getMessage());
                        this.e = null;
                    } else {
                        Gson gson = new Gson();
                        AnalysisResult result = gson.fromJson(data, AnalysisResult.class);
                        Log.d("info", String.valueOf(result));
                        mEditText.append("Image format: " + result.metadata.format + "\n");
                        mEditText.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                        mEditText.append("Clip Art Type: " + result.imageType.clipArtType + "\n");
                        mEditText.append("Line Drawing Type: " + result.imageType.lineDrawingType + "\n");
                        mEditText.append("Is Adult Content:" + result.adult.isAdultContent + "\n");
                        mEditText.append("Adult score:" + result.adult.adultScore + "\n");
                        mEditText.append("Is Racy Content:" + result.adult.isRacyContent + "\n");
                        mEditText.append("Racy score:" + result.adult.racyScore + "\n\n") ;

                        for (Category category: result.categories) {
                            mEditText.append("Category: " + category.name + ", score: " + category.score + "\n");
                        }

                        mEditText.append("\n");
                        int faceCount = 0;
                        for (Face face: result.faces) {
                            faceCount++;
                            mEditText.append("face " + faceCount + ", gender:" + face.gender + "(score: " + face.genderScore + "), age: " + + face.age + "\n");
                            mEditText.append("    left: " + face.faceRectangle.left +  ",  top: " + face.faceRectangle.top + ", width: " + face.faceRectangle.width + "  height: " + face.faceRectangle.height + "\n" );
                        }
                        if (faceCount == 0) {
                            mEditText.append("No face is detected");
                        }
                        mEditText.append("\n");

                        mEditText.append("\nDominant Color Foreground :" + result.color.dominantColorForeground + "\n");
                        mEditText.append("Dominant Color Background :" + result.color.dominantColorBackground + "\n");

                        mEditText.append("\n--- Raw Data ---\n\n");
                        mEditText.append(data);
                        mEditText.setSelection(0);
                    }

                    mButtonSelectImage.setEnabled(true);
                }
            }
        }








