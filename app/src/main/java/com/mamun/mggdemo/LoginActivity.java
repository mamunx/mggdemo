package com.mamun.mggdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    ProgressBar progressBar;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private final static String URL = "http://54.218.102.254/scarecrow/json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText)findViewById(R.id.input_email);
        etPassword = (EditText)findViewById(R.id.input_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")){
                    hideKeyboard(LoginActivity.this);
                    progressBar.setVisibility(View.VISIBLE);
                    stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Snackbar.make(btnLogin, jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                                if(jsonObject.getString("success").equals("true")){
                                    SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("email", etEmail.getText().toString().trim());
                                    editor.apply();

                                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                    progressBar.setVisibility(View.GONE);
                                }
                                else if(jsonObject.getString("success").equals("false")){
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> paramHashMap = new HashMap<>();
                            paramHashMap.put("tag", "login");
                            paramHashMap.put("email", etEmail.getText().toString().trim());
                            paramHashMap.put("password", etPassword.getText().toString());
                            return paramHashMap;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
                else {
                    Snackbar.make(btnLogin, "Enter Email and Password", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
