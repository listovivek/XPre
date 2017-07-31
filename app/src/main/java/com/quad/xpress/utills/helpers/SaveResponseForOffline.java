package com.quad.xpress.utills.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.client.Response;
import retrofit.mime.TypedInput;

/**
 * Created by Venkatesh on 18-06-16.
 */
public class SaveResponseForOffline {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public SaveResponseForOffline(Context context) {
        sharedpreferences = context.getSharedPreferences(SharedPrefUtils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

    }

    public void save(Response response, String toSaveKey) {
        TypedInput body = response.getBody();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            // Prints the correct String representation of body.
            //  System.out.println(out);
            editor.putString(toSaveKey, out.toString());
            editor.commit();

            Log.v("SaveResponseForOffline", " toSaveKey " + toSaveKey + " out.toString() " + out.toString());
            //  Log.v("getData", "Response " + out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
