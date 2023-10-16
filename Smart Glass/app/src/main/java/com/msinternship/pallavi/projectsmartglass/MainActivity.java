package com.msinternship.pallavi.projectsmartglass;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    private FaceServiceClient faceServiceClient =  new FaceServiceRestClient("84920306285a4bcfb3b5d8bfad1330dd");
    EmotionServiceClient emotionServiceClient = new EmotionServiceRestClient("ce8a7f82923246daa9a34f28427acb99");
    private VisionServiceClient visionServiceClient = new VisionServiceRestClient("d5b2b7419f4549c7bff788a8dce63064");

    private final int PICK_IMAGE = 1;
    private ProgressDialog detectionProgressDialog;
    private Bitmap bitmap1;
    private ArrayList<String> charecter = new ArrayList<>();
    private ArrayList<String> emotion= new ArrayList<>();
    private StringBuilder description = new StringBuilder();
    private int n;
    private TextToSpeech tts ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.button1);
        tts = new TextToSpeech(this,this);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(gallIntent, "Select Picture"), PICK_IMAGE);
            }
        });
        
        detectionProgressDialog = new ProgressDialog(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageBitmap(bitmap);
                displayDescription(bitmap);
                displayEmotion(bitmap);
                detectAndFrame(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private void displayDescription(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        detectTask1 detect1 = new detectTask1();

        detect1.execute(inputStream);
        //imageBitmap.recycle();
    }


    public class detectTask1 extends AsyncTask<InputStream, String, AnalysisResult> {

        ProgressDialog detectionProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected AnalysisResult doInBackground(InputStream... params) {
            try {
                publishProgress("Detecting...");
                AnalysisResult result = visionServiceClient.describe(params[0], 1);
                if (result == null)
                {
                    publishProgress("Detection Finished. Nothing detected");
                    return null;
                }
                publishProgress(
                        String.format("Detection Finished."));
                return result;
            } catch (Exception e) {
                publishProgress("Detection failed");
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            //TODO: show progress dialog
            detectionProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            //TODO: update progress

            detectionProgressDialog.setMessage(progress[0]);
        }
        @Override
        protected void onPostExecute(AnalysisResult result) {
            //TODO: update face frames
            detectionProgressDialog.dismiss();
            if (result == null) return;
            double x =result.description.captions.get(0).confidence*100;
            int y=(int)x;

            String s;
            s=result.description.captions.get(0).text + " with confidence " + y+" percent.\n";
            description.append(s);
            Log.i("Description", description.toString());

            Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();
        }

    }



    private void displayEmotion(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        detectTask detect = new detectTask();
        detect.execute(inputStream);
        bitmap1 = imageBitmap;
    }
    public class detectTask extends AsyncTask<InputStream, String, List<RecognizeResult>> {

        ProgressDialog detectionProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected List<RecognizeResult> doInBackground(InputStream... params) {
            try {
                publishProgress("Detecting...");
                List<RecognizeResult> result = emotionServiceClient.recognizeImage(params[0]);
                if (result == null)
                {
                    publishProgress("Detection Finished. Nothing detected");
                    return null;
                }
                publishProgress(
                        String.format("Detection Finished."));
                return result;
            } catch (Exception e) {
                publishProgress("Detection failed");
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            //TODO: show progress dialog
            detectionProgressDialog.show();
        }
        @Override
        protected void onProgressUpdate(String... progress) {
            //TODO: update progress

            detectionProgressDialog.setMessage(progress[0]);
        }
        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            //TODO: update face frames
            detectionProgressDialog.dismiss();
            if (result == null) return;
            for (int i = 0; i < result.size(); i++) {
                RecognizeResult recognizeResult = result.get(i);
                double emot= max(recognizeResult.scores.happiness, recognizeResult.scores.anger, recognizeResult.scores.fear, recognizeResult.scores.neutral, recognizeResult.scores.surprise, recognizeResult.scores.disgust, recognizeResult.scores.contempt, recognizeResult.scores.sadness);
                if(emot ==recognizeResult.scores.happiness ) {
                    emotion.add(i,"feeling Happy!\n");
                }
                if(emot ==recognizeResult.scores.anger ) {
                    emotion.add(i,"feeling Angry!\n");
                }
                if(emot ==recognizeResult.scores.fear ) {
                    emotion.add(i,"feeling Scared!\n");
                }
                if(emot ==recognizeResult.scores.neutral ) {
                    emotion.add(i,"feeling Neutral!\n");

                }
                if(emot ==recognizeResult.scores.surprise ) {
                    emotion.add(i,"feeling Surpriesd!\n");

                }
                if(emot ==recognizeResult.scores.disgust ) {
                    emotion.add(i,"feeling Disgusted!\n");

                }
                if(emot ==recognizeResult.scores.contempt ) {
                    emotion.add(i,"feeling Contempt!\n");

                }
                if(emot ==recognizeResult.scores.sadness ) {
                    emotion.add(i,"Sad!\n");

                }
            }

            Log.i("Description", emotion.toString());

        }
    }

    double max(double a, double b, double c, double d, double e, double f, double g, double h) {
        double a1 = max1(a, b, c);
        double a2 = max1(a1, d,e);
        double a3 = max1(a2, f,g);
        double fin = max1(a3,h, 0.00);
        return fin;
    }

    double max1(double a, double b, double c) {
        if(a>b) {
            if(b>c) {
                return a;
            }
            else if (a>c) {
                return a;
            }
            else {
                return c;
            }
        }
        else {
            if(b>c) {
                return b;
            }
            else {
                return c;
            }
        }
    }



    private void detectAndFrame(final Bitmap imageBitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    FaceServiceClient.FaceAttributeType faceAttributeType[] = {
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Smile
                    };
                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    true,        // returnFaceLandmarks
                                    faceAttributeType           // returnFaceAttributes: a string like "age, gender"
                            );
                            if (result == null)
                            {
                                publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(
                                    String.format("Detection Finished. %d face(s) detected",
                                            result.length));
                            return result;
                        } catch (Exception e) {
                            publishProgress("Detection failed");
                            return null;
                        }
                    }
                    @Override
                    protected void onPreExecute() {
                        detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        detectionProgressDialog.dismiss();
                        if (result == null) return;
                        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                        imageView.setImageBitmap(drawFaceRectanglesOnBitmap(imageBitmap, result));
                        imageBitmap.recycle();
                        try {
                            displaychar(result);
                            makeDescription();

                        } catch (Exception e) {};
                    }

                };
        detectTask.execute(inputStream);
    }


    void displaychar(Face[] result) throws InterruptedException {
        int i;

        n=result.length;

        for(i=0;i<result.length;i++){
            String s;
            s="A " + result[i].faceAttributes.gender + " of age "+result[i].faceAttributes.age+" years ";
            charecter.add(i,s);

        }

    }
    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {
            for (Face face : faces) {
                com.microsoft.projectoxford.face.contract.FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }


    private void makeDescription(){

        for (int i=0;i<n;i++){

            description.append(charecter.get(i));
            description.append(emotion.get(i));

        }

        TextView txt =(TextView)findViewById(R.id.textView);
        txt.setText(description.toString());
        speakOut();
        description.setLength(0);


    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }



    private void speakOut() {
        String text = description.toString();
        tts.setPitch(-10);
        tts.setSpeechRate(-50);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


}
