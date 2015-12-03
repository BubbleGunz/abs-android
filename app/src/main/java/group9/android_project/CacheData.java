package group9.android_project;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Fetes on 2015-12-03.
 */
public class CacheData {
    public static void createCachedFile (Context context, String key, String jsonObject) throws IOException {

        String tempFile = null;
        FileOutputStream fos = context.openFileOutput (key, Context.MODE_PRIVATE);
        fos.write(jsonObject.getBytes());
        fos.close ();

    }

    public static JSONObject readCachedFile (Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput (key);
        JSONObject jsonObject = new JSONObject();
        Scanner scanner = new Scanner(fis);
        try {
            jsonObject = new JSONObject(scanner.nextLine());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
