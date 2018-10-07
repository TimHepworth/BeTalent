package com.betalent.betalent;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.betalent.betalent.Local.CampaignDAO;
import com.betalent.betalent.Model.BeTalentDB;
import com.betalent.betalent.Model.Campaign;
import com.betalent.betalent.Model.Question;
import com.betalent.betalent.Model.QuestionChoice;
import com.betalent.betalent.Model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txtEmail_til;
    private TextInputLayout txtPassword_til;
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private Boolean dataAvailable;
    private BeTalentDB betalentDb;
    private String password;
    private String emailAddress;
    private  ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        betalentDb = BeTalentDB.getInstance(LoginActivity.this);

        dataAvailable = intent.getBooleanExtra("DataAvailable", false);
        password = intent.getStringExtra("Password");
        emailAddress = intent.getStringExtra("EmailAddress");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        txtEmail_til = findViewById(R.id.txtEmail_til);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword_til = findViewById(R.id.txtPassword_til);
        txtPassword = findViewById(R.id.txtPassword);
        pBar = findViewById(R.id.loginSpinner);

        //
        //  Blank any error message as something is entered in a field
        //

        txtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           }

            @Override
            public void afterTextChanged(Editable s) {
                txtEmail_til.setError("");
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtPassword_til.setError("");
            }
        });

        //
        //  Handle login button click
        //

        Button clickButton = (Button) findViewById(R.id.btnLogin);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Boolean errorFound = false;

                if (txtEmailAddress.getText().toString().equals("")) {
                    txtEmail_til.setError(getString(R.string.err_field_required));
                    errorFound = true;
                }

                if (txtPassword.getText().toString().equals("")) {
                    txtPassword_til.setError(getString(R.string.err_field_required));
                    errorFound = true;
                }

                if (!errorFound) {

                    //
                    //  Check for valid login - if valid, check if we have the latest
                    //  version of the campaigns - if not then download them to the
                    //  local database
                    //

                    if (dataAvailable) {

                        if (!txtEmailAddress.getText().toString().equals(emailAddress) ||
                                !txtPassword.getText().toString().equals(password)    ) {
                            txtEmail_til.setError(getString(R.string.err_invalid_login));
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                    } else {

                        //
                        //  Show spinning circle
                        //

                        pBar.setVisibility(View.VISIBLE);
                        pBar.bringToFront();

                        //
                        //  Get the user details from the server
                        //

                        String url = BeTalentProperties.BASE_URL + "getUser";
                        url += "?sEmailAddress=" + txtEmailAddress.getText().toString();
                        url += "&sPassword=" + txtPassword.getText().toString();

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        //
                                        //  Hide spinning circle
                                        //

                                        pBar.setVisibility(View.INVISIBLE);

                                        try {

                                            if (!response.getBoolean("login_ok")) {

                                                BeTalentUtils.showAlert(LoginActivity.this,
                                                        getString(R.string.err_invalid_login),
                                                        getString(R.string.err_login_details));
                                            } else {

                                                //
                                                //  Login is OK, so download the latest campaign information etc.
                                                //

                                                User user = new User(response.getInt("user_id"),
                                                        response.getInt("company_id"),
                                                        response.getString("email_address"),
                                                        response.getString("password"),
                                                        response.getString("forename"),
                                                        response.getString("surname"),
                                                        response.getString("employee_level"),
                                                        true);

                                                betalentDb.getUserDao().insertUser(user);

                                                BeTalentProperties.getInstance().userId = response.getInt("user_id");
                                                BeTalentProperties.getInstance().emailAddress = response.getString("email_address");
                                                BeTalentProperties.getInstance().employeeLevel = response.getString("employee_level");

                                                //
                                                //  Show spinning circle
                                                //

                                                pBar.setVisibility(View.VISIBLE);
                                                pBar.bringToFront();

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        RefreshData();

                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }) .start();

                                            }

                                        } catch (JSONException e) {
                                            Toast.makeText(LoginActivity.this, getString(R.string.err_json_parse), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(LoginActivity.this, getString(R.string.err_unexpected) + ' ' + error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                        BeTalentProperties.getInstance().mRequestQueue.add(jsObjRequest);
                    }

                }

            }
        });
    }

    private void RefreshData() {

        System.out.println("Refreshing data ...");

        String url = BeTalentProperties.BASE_URL + "getUserCampaigns";
        url += "?lUserId=" + BeTalentProperties.getInstance().userId;
        url += "&sEmployeeLevel=" + BeTalentProperties.getInstance().employeeLevel;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            betalentDb = BeTalentDB.getInstance(LoginActivity.this);

                            for (int i = 0; i < response.length(); i++) {

                                System.out.println("item #" + i + " - " + response.getJSONObject(i));

                                JSONObject jsonObj = response.getJSONObject(i);

                                Campaign campaign = new Campaign(jsonObj.getInt("CampaignUserId"),
                                        jsonObj.getInt("CampaignId"),
                                        jsonObj.getInt("CompanyId"),
                                        jsonObj.getString("CampaignName"),
                                        jsonObj.getLong("EndDate"),
                                        jsonObj.getString("AssesseeForename"),
                                        jsonObj.getString("AssesseeSurname"),
                                        jsonObj.getString("AssessmentStatus"),
                                        jsonObj.getString("ProductIcon"));

                                DownloadImage(jsonObj.getString("ProductIcon"), jsonObj.getInt("CampaignId"), LoginActivity.this);

                                betalentDb.getCampaignDao().insertCampaign(campaign);

                                JSONArray jsonQuestions = jsonObj.getJSONArray("Questions");

                                for (int j = 0; j < jsonQuestions.length(); j++) {

                                    JSONObject jsonQuestion = jsonQuestions.getJSONObject(j);

                                    Question question = new Question(jsonQuestion.getInt("QuestionId"),
                                            jsonQuestion.getString("SectionName"),
                                            jsonQuestion.getInt("PageNumber"),
                                            jsonQuestion.getInt("DisplayOrder"),
                                            jsonQuestion.getInt("NumScaleChoices"),
                                            jsonQuestion.getString("QuestionType"),
                                            jsonQuestion.getString("QuestionTextSelf"),
                                            jsonQuestion.getString("QuestionTextOthers"));

                                    betalentDb.getQuestionDao().insertQuestion(question);

                                    JSONArray jsonQuestionChoices = jsonQuestion.getJSONArray("QuestionChoices");

                                    for (int k = 0; k < jsonQuestionChoices.length(); k++) {

                                        JSONObject jsonChoice = jsonQuestionChoices.getJSONObject(k);

                                        QuestionChoice choice = new QuestionChoice(jsonChoice.getInt("QuestionChoiceId"),
                                                jsonChoice.getInt("QuestionId"),
                                                jsonChoice.getString("ChoiceText"),
                                                jsonChoice.getInt("Score"),
                                                jsonChoice.getInt("PersonalAttributeId"),
                                                jsonChoice.getInt("DisplayOrder"));

                                        betalentDb.getQuestionChoiceDao().insertQuestionChoice(choice);

                                    }

                                }

                            }

                            //
                            //  Hide spinning circle
                            //

                            pBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, getString(R.string.err_json_parse), Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(LoginActivity.this, getString(R.string.err_unexpected) + ' ' + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        BeTalentProperties.getInstance().mRequestQueue.add(jsObjRequest);
    }

    private void DownloadImage(String productIcon, final int campaignId, Context ctx) {

        String url = BeTalentProperties.ICON_URL + productIcon;

        Glide.with(ctx)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(200,200) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource, campaignId, LoginActivity.this);
                    }
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
//                        saveImage(resource);
//                    }
                });
    }

    private String saveImage(Bitmap image, int campaignId, Context ctx) {
        String savedImagePath = null;

        String imageFileName = "ICON_" + campaignId + ".jpg";
        File imageFile = new File(ctx.getFilesDir(), imageFileName);
        savedImagePath = imageFile.getAbsolutePath();

        try {
            OutputStream fOut = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedImagePath;
    }

}
