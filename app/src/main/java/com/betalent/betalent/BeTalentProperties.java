package com.betalent.betalent;

import com.android.volley.RequestQueue;

public class BeTalentProperties {

    public static final String BASE_URL = "https://services.betalent.com/betalent/betalentservice.asmx/";
    public static final String ICON_URL = "http://assess.betalent.com/images/productlogos/";
    public static final String CARD_URL = "http://assess.betalent.com/uploadedfiles/";

    private static BeTalentProperties mInstance = null;

    public int companyId;
    public int userId;
    public String emailAddress;
    public String employeeLevel;
    public boolean connectionAvailable = false;
    public RequestQueue mRequestQueue;

    protected BeTalentProperties() {}

    public static synchronized BeTalentProperties getInstance() {
        if (mInstance == null) {
            mInstance = new BeTalentProperties();
        }
        return mInstance;
    }
}