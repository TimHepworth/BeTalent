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
import android.util.Log;
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
import com.betalent.betalent.Model.ProductSection;
import com.betalent.betalent.Model.Question;
import com.betalent.betalent.Model.QuestionChoice;
import com.betalent.betalent.Model.Tag;
import com.betalent.betalent.Model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
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
    private ProgressBar pBar;

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

                                                        GetQuestions();

                                                        GetTags();

                                                        GetProductSections();

                                                        GetScaleChoices();

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

    private void GetQuestions() {

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
                                        jsonObj.getInt("CurrentQuestionNo"),
                                        jsonObj.getString("CampaignName"),
                                        jsonObj.getLong("EndDate"),
                                        jsonObj.getString("AssesseeForename"),
                                        jsonObj.getString("AssesseeSurname"),
                                        jsonObj.getString("AssessmentStatus"),
                                        jsonObj.getString("ProductIcon"),
                                        jsonObj.getString("ProductType"),
                                        jsonObj.getInt("NumCardsRequired"),
                                        jsonObj.getString("TagType"));

                                DownloadImage(BeTalentProperties.ICON_URL + jsonObj.getString("ProductIcon"),
                                        "ICON_" + jsonObj.getInt("CampaignId") + ".jpg", LoginActivity.this);

                                betalentDb.getCampaignDao().insertCampaign(campaign);

                                JSONArray jsonQuestions = jsonObj.getJSONArray("Questions");

                                for (int j = 0; j < jsonQuestions.length(); j++) {

                                    JSONObject jsonQuestion = jsonQuestions.getJSONObject(j);

                                    Question question = new Question(jsonQuestion.getInt("QuestionId"),
                                            campaign.getCampaignId(),
                                            jsonQuestion.getString("SectionName"),
                                            jsonQuestion.getInt("PageNumber"),
                                            jsonQuestion.getInt("DisplayOrder"),
                                            jsonQuestion.getInt("NumScaleChoices"),
                                            j + 1,
                                            jsonQuestion.getInt("ScaleId"),
                                            jsonQuestion.getString("QuestionType"),
                                            jsonQuestion.getString("QuestionTextSelf"),
                                            jsonQuestion.getString("QuestionTextOthers"));

                                    betalentDb.getQuestionDao().insertQuestion(question);

                                    JSONArray jsonQuestionChoices = jsonQuestion.getJSONArray("QuestionChoices");

                                    for (int k = 0; k < jsonQuestionChoices.length(); k++) {

                                        JSONObject jsonChoice = jsonQuestionChoices.getJSONObject(k);

                                        System.out.println("adding choice: questionId: " + jsonQuestion.getInt("QuestionId") + " questionChoiceId: " + jsonChoice.getInt("QuestionChoiceId"));

                                        QuestionChoice choice = new QuestionChoice(jsonChoice.getInt("QuestionChoiceId"),
                                                jsonQuestion.getInt("QuestionId"),
                                                jsonQuestion.getInt("ScaleId"),
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

    private void GetScaleChoices() {

        System.out.println("Getting scale choices ...");

        String url = BeTalentProperties.BASE_URL + "getScaleChoices";
//        url += "?lUserId=" + BeTalentProperties.getInstance().userId;
//        url += "&sEmployeeLevel=" + BeTalentProperties.getInstance().employeeLevel;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            betalentDb = BeTalentDB.getInstance(LoginActivity.this);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObj = response.getJSONObject(i);

                                QuestionChoice choice = new QuestionChoice(jsonObj.getInt("QuestionChoiceId"),
                                        jsonObj.getInt("QuestionId"),
                                        jsonObj.getInt("ScaleId"),
                                        jsonObj.getString("ChoiceText"),
                                        jsonObj.getInt("Score"),
                                        jsonObj.getInt("PersonalAttributeId"),
                                        jsonObj.getInt("DisplayOrder"));

                                betalentDb.getQuestionChoiceDao().insertQuestionChoice(choice);

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

    private void GetProductSections() {

        System.out.println("Getting product sections ...");

        String url = BeTalentProperties.BASE_URL + "getProductSections";
        url += "?lUserId=" + BeTalentProperties.getInstance().userId;
        url += "&sEmployeeLevel=" + BeTalentProperties.getInstance().employeeLevel;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            betalentDb = BeTalentDB.getInstance(LoginActivity.this);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObj = response.getJSONObject(i);

                                ProductSection productSection = new ProductSection(jsonObj.getInt("ProductSectionId"),
                                        jsonObj.getString("SectionName"),
                                        jsonObj.getInt("DisplayOrder"),
                                        jsonObj.getInt("ScaleId"),
                                        jsonObj.getString("SectionInstructions"));

                                betalentDb.getProductSectionDao().insertProductSection(productSection);

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

    private void GetTags() {

        System.out.println("Getting tags ...");

        String url = BeTalentProperties.BASE_URL + "getTags";
//        url += "?lUserId=" + BeTalentProperties.getInstance().userId;
//        url += "&sEmployeeLevel=" + BeTalentProperties.getInstance().employeeLevel;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            betalentDb = BeTalentDB.getInstance(LoginActivity.this);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObj = response.getJSONObject(i);

                                Tag tag = new Tag(jsonObj.getInt("TagId"),
                                        jsonObj.getString("TagName"),
                                        jsonObj.getString("CardImageFile"),
                                        jsonObj.getString("CardText"),
                                        jsonObj.getString("BESTType"));

                                DownloadImage(BeTalentProperties.CARD_URL + jsonObj.getString("CardImageFile"),
                                        "CARD_" + jsonObj.getInt("TagId") + ".jpg", LoginActivity.this);

                                betalentDb.getTagDao().insertTag(tag);

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

    private void DownloadImage(String url, final String fileName, Context ctx) {

        Glide.with(ctx)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource, fileName, LoginActivity.this);
                    }
                });
    }

    private String saveImage(Bitmap image, String imageFileName, Context ctx) {
        String savedImagePath = null;

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
