package com.google.firebase.udacity.skincancerdetectorhackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.udacity.skincancerdetectorhackathon.ml.Cancermodel;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;

public class SkinCancer extends AppCompatActivity {
    private Button buttonPicture;
    private Button skinCancerPositive;
    private Button skinCancerNegative;
    private ImageView imageViewPicture;
    private TextView textViewResult;
    private TextView textViewDescription;
    private LinearLayout linearLayoutSkinCancer;
    private String skinCancerTreatmentUrl = "https://www.cancer.gov/types/skin/patient/skin-treatment-pdq";
    private String skinCancerPreventionUrl = "https://www.skincancer.org/skin-cancer-prevention/";
    private String melanomaDesc = "Melanoma is the most severe type of skin cancer, and not as common, but grows and spreads very quickly, so early diagnosis is key. Melanomas can appear as either a pigmented mole, as well as other forms. When nearing the end stages, melanoma is virtually untreatable. Risk factors include fair skin, sun-exposure, and a weakened immune system. We recommend that you receive a professional clinical opinion.";
    private String basalDesc = "Basal Cell Carcinoma is the most common type of skin cancer. These types of tumors grow slowly, and are usually treatable. Risk factors include chronic sun exposure, radiation therapy, fair skin, increasing age, immune-suppressing drugs, arsenic exposure, and genetic history. We recommend that you receive a professional clinical opinion.";
    private String squamousDesc = "Squamous Cell Carcinoma is the second most type of skin cancer, and can come in various farms. Early diagnosis is the key to reducing risk, but given time to spread, it can destroy nearby tissue. Risk factors are UV radiation and agents that cause a mutation in the DNA squamous cell. WE recommend that you receive a professional clinical opinion.";
    private String benignDesc = "Although skin cancer has not been detected, skin cancer is the most common type of cancer, and it is important to prevent skin tumors.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_cancer);
        //initializing all the views (buttons..linear layouts)
        buttonPicture = (Button) findViewById(R.id.button_Picture);
        skinCancerPositive = new Button(this);
        skinCancerNegative = new Button(this);
        imageViewPicture = (ImageView) findViewById(R.id.imageView_PhotoSlot);
        textViewResult = (TextView) findViewById(R.id.textView_Result);
        textViewDescription = (TextView) findViewById(R.id.textView_Description);
        linearLayoutSkinCancer = (LinearLayout) findViewById(R.id.linear_layout_skin_cancer);

        //setting dimensions of positive/negative buttons
        skinCancerPositive.setText("Skin Cancer Treatment");
        skinCancerPositive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        skinCancerNegative.setText("Skin Cancer Prevention");
        skinCancerNegative.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //initially remove positive or negative buttons. (when reloaded.. should be romved)
        linearLayoutSkinCancer.removeView(skinCancerPositive);
        linearLayoutSkinCancer.removeView(skinCancerNegative);
        textViewResult.setText("");
        textViewDescription.setText("");

        //checks for camera permissions when clikcing the pciture buttons, and start intent to take the picture
        buttonPicture.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(view.getContext(),
                    android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(SkinCancer.this,
                        android.Manifest.permission.CAMERA)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(SkinCancer.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            0);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            //goes to camera screen and starts intent to take picture from camera app
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 0);
        });

        //if skin cancer positive is displayes, go to url for treatment page
        skinCancerPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInformationSearchIntent = new Intent(Intent.ACTION_VIEW);
                startInformationSearchIntent.setData(Uri.parse(skinCancerTreatmentUrl));
                startActivity(startInformationSearchIntent);
            }
        });

        //if skin cancer negative is displayes, go to url for prevention page
        skinCancerNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInformationSearchIntent = new Intent(Intent.ACTION_VIEW);
                startInformationSearchIntent.setData(Uri.parse(skinCancerPreventionUrl));
                startActivity(startInformationSearchIntent);
            }
        });
    }

    //giving the picture to be displayed on app after taking the picture

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            //gets data from intent
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            //actual picture, set imageview to picture
            imageViewPicture.setImageBitmap(theImage);
            //run machine learning model, takes image bitmap and returns whether you have skin cancer or not
            boolean isCancer = true;
            try {
                String cancerType = isSkinCancer(theImage);
                if (cancerType.toLowerCase().contains("benign")) {
                    isCancer = false;
                    textViewResult.setText("Skin Cancer Not Detected");
                    textViewDescription.setText(benignDesc);
                }
                if (cancerType.toLowerCase().contains("melanoma")) {
                    textViewResult.setText("Skin Cancer Detected. Likely: Melanoma");
                    textViewDescription.setText(melanomaDesc);
                }
                if (cancerType.toLowerCase().contains("basal")) {
                    textViewResult.setText("Skin Cancer Detected. Likely: Basal Cell Carcinoma");
                    textViewDescription.setText(basalDesc);
                }
                if (cancerType.toLowerCase().contains("squamous")) {
                    textViewResult.setText("Skin Cancer Detected. Likely: Squamous Cell Carcinoma");
                    textViewDescription.setText(squamousDesc);
                }
                //if skin cancer, add skin cancer button and add text of skin cancer detected,
                // otherwise set the text to skin cancer not detected and show button for prevention
                linearLayoutSkinCancer.removeView(skinCancerNegative);
                linearLayoutSkinCancer.removeView(skinCancerPositive);
                if (isCancer) {
                    linearLayoutSkinCancer.addView(skinCancerPositive);
                } else {
                    linearLayoutSkinCancer.addView(skinCancerNegative);
                }
                //if machine model doesn't work, then tell user to retake picture, and remove positive/negative buttons
            } catch (IOException e) {
                textViewResult.setText("Retake picture");
                textViewDescription.setText("");
                linearLayoutSkinCancer.removeView(skinCancerNegative);
                linearLayoutSkinCancer.removeView(skinCancerPositive);
                e.printStackTrace();
            }
        }
    }
//uses machine learning model to determine if user has skin cancer by the picture provided as a parameter,
    //and returns true or false accordingly
    private String isSkinCancer(Bitmap bitmap) throws IOException{

        Cancermodel model = Cancermodel.newInstance(this);
        // Creates inputs for reference.
        TensorImage image = TensorImage.fromBitmap(bitmap);

        // Runs model inference and gets result.
        Cancermodel.Outputs outputs = model.process(image);
        List<Category> probability = outputs.getProbabilityAsCategoryList();

        Category bestMatch = null;
        float max_score = Integer.MIN_VALUE;

        for (Category curr_cat : probability) {
            Log.d("Category score:", curr_cat.getLabel() + ": " + curr_cat.getScore());
            if (curr_cat.getScore() > max_score) {
                max_score = curr_cat.getScore();
                bestMatch = curr_cat;
            }
        }

        // Releases model resources if no longer used.
        model.close();

        return bestMatch.getLabel();
    }
}