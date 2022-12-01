package com.zybooks.ee408_graduation;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public abstract class ApiRequest {

    public ApiRequest(Context context, String url, JSONObject params)
    {
        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PostCallback(response, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PostCallback(null, error);
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(registerRequest);
    }

    public abstract void PostCallback(JSONObject jsonObject, VolleyError volleyError);
}
