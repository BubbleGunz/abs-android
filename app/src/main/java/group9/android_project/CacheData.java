package group9.android_project;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Fetes on 2015-12-03.
 */
public class CacheData {
    public static void createCachedFile (Context context, ArrayList<Vacation> toCache) throws IOException {

        //String content = toCache;
        File file;
        FileOutputStream outputStream;
        try {
            // file = File.createTempFile("MyCache", null, getCacheDir());
            file = new File(context.getCacheDir(), "MyCache");

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(toCache);
            oos.close();

            /*outputStream = new FileOutputStream(file);
            outputStream.write();
            outputStream.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
         /*final File cache_dir = context.getCacheDir();
            final File suspend_f = new File(cache_dir.getAbsoluteFile() + File.separator + key);

            FileOutputStream   fos  = null;
            ObjectOutputStream oos  = null;
            boolean            keep = true;

            try {
                fos = new FileOutputStream(suspend_f);
                oos = new ObjectOutputStream(fos);

                oos.writeBytes(toCache);
            }
            catch (Exception e) {
                keep = false;
                Log.e("MyAppName", "failed to suspend", e);
            }
            finally {
                try {
                    if (oos != null)   oos.close();
                    if (fos != null)   fos.close();
                    if (keep == false) suspend_f.delete();
                }
                catch (Exception e) { /* do nothing */
        }



    public static ArrayList<Vacation> readCachedFile (Context context) throws IOException {

        BufferedReader input = null;
        ArrayList<Vacation> vacationArrayList = null;
        JSONObject jsonReturn = null;
        File file = null;
        ArrayList<Vacation> vacations = null;
        try {
            file = new File(context.getCacheDir(), "MyCache");

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                vacations = (ArrayList<Vacation>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();



            /*input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            BufferedReader br = new BufferedReader(input);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();*/


            /*StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }*/
            /*JSONArray jsonResponse = new JSONArray(sb.toString());
            for (int i = 0 ; i<jsonResponse.length();i++) {
                JSONObject vacationJson =  jsonResponse.getJSONObject(i);
                Vacation vacation = new Vacation();
                vacation.title = vacationJson.getString("title");

                vacationArrayList.add(vacation);
            }
            jsonReturn.put("vacations", vacationArrayList);


            vacationArrayList = new ArrayList<Vacation>();
            JSONObject json = new JSONObject(sb.toString());
            vacationArrayList = (ArrayList<Vacation>) json.get("vacs");*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vacations;

        /*final File cache_dir = context.getCacheDir();
        final File suspend_f = new File(cache_dir.getAbsoluteFile() + File.separator + key);
        JSONObject json = new JSONObject();
        FileInputStream   fis  = null;
        ObjectInputStream ois  = null;
        boolean            keep = true;

        try {
            fis = new FileInputStream(suspend_f);
            ois = new ObjectInputStream(fis);
            Object lol = ois.readByte();
        }
        catch (Exception e) {
            keep = false;
            Log.e("MyAppName", "failed to suspend", e);
        }
        finally {
            try {
                if (ois != null)   ois.close();
                if (fis != null)   fis.close();
                //if (keep == false) suspend_f.delete();
            }
            catch (Exception e) { /* do nothing */


    }
}
