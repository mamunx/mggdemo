package com.mamun.mggdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class DashboardActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvToken;
    private ProgressBar progressBar;
    private Button btnLogout;

    private static final String URL ="http://54.218.102.254/scarecrow/json.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvName = (TextView)findViewById(R.id.textViewName);
        tvEmail = (TextView)findViewById(R.id.textViewEmail);
        tvToken = (TextView)findViewById(R.id.textViewToken);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);
        btnLogout = (Button)findViewById(R.id.btn_logout);

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        final SharedPreferences.Editor editor = settings.edit();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    tvName.setText(jsonObject.getString("name"));
                    tvEmail.setText(jsonObject.getString("email"));
                    tvToken.setText(jsonObject.getString("token"));
                    progressBar.setVisibility(View.GONE);
                    btnLogout.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> paramHash = new HashMap<>();
                paramHash.put("tag", "getData");
                return paramHash;
            }
        };
        requestQueue.add(stringRequest);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("email");
                editor.apply();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
