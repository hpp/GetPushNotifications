package io.hpp.getpushnotifications;

import android.content.Context;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by izzy on 2/8/16.
 */
public class NotificationsResponseManager {
    static String TAG = "NotificationResponseManager";
    ArrayList<UpdateNavigationDrawerListener> updateDrawerListeners;
    RequestQueue requestQueue;


    public NotificationsResponseManager(Context context){
        requestQueue = Volley.newRequestQueue(context);
        updateDrawerListeners = new ArrayList<>();
    }

    public void postNotificationForResponse(){

        String url = "http://bsg.redmatmediabizapp.com/admin/push/getPushNotifications.php";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("AppVersion", "BSG");
        jsonParams.put("userID", "2");


        CustomRequest jsObjRequest = new CustomRequest(
                Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (UpdateNavigationDrawerListener listener : updateDrawerListeners) {
                            listener.setNavigationDrawerFromJson(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO Handle Error
                        Log.d(TAG, error.getMessage());
                    }
                });

        requestQueue.add(jsObjRequest);
    }

    public void addUpdateNavigationDrawerListener(UpdateNavigationDrawerListener list){
        updateDrawerListeners.add(list);
    }

    public interface UpdateNavigationDrawerListener {
        public void setNavigationDrawerFromJson(JSONObject items);
    }

    public static String[] getAllValuesForKeyFromResponse(String key, JSONObject response){
        String[] values = {};
        try {
            JSONArray messages = response.getJSONArray("results");
            values = new String[messages.length()];
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                values[i] = message.getString(key);
            }
            return values;
        } catch (JSONException e){
            Log.d(TAG,e.toString());
        }
        return values;
    }
}
