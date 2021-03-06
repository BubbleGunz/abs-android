package group9.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2015-11-06.
 */
public class AsyncCall extends AsyncTask<AsyncCallInfo,Void,JSONObject> {
    @Override
    protected JSONObject doInBackground(AsyncCallInfo... params) {
        String sw = params[0].command;
        Log.d("In background ",sw);
        JSONObject response = null;

        switch(sw){
            case "CreateUser" :
            {
                JSONObject json = params[0].userInfo;
                response = ApiRequest.CreateUser(json);
                return response;
            }
            case "GetToken":
            {
                User user = params[0].user;
                response = ApiRequest.GetToken(user,params[0].context);
                return response;
            }
            case "AddFriend":
            {
                User user = params[0].user;
                response = ApiRequest.AddFriend(user,params[0].context);
                return response;
            }
            case "GetFriends": {
                User user = params[0].user;
                response = ApiRequest.GetFriends(user, params[0].context);
                return response;
            }
            case "GetVacations": {
                User user = params[0].user;
                response = ApiRequest.GetVacations(user, params[0].context);
                return response;
            }
            case "GetMemories": {
                Vacation vacation = params[0].vacation;
                response = ApiRequest.GetMemories(vacation, params[0].context);
                return response;
            }
            case "GetMedia": {
                Memory memory = params[0].memory;
                response = ApiRequest.GetMedia(memory, params[0].context);
                return response;
            }
            case "GetBitmap": {
                String url= params[0].url;
                response = ApiRequest.GetBitmap(url, params[0].context);
                return response;
            }
            case "RemoveFriend": {
                User user= params[0].user;
                response = ApiRequest.RemoveFriend(user, params[0].context);
                return response;
            }
            case "AddVacation": {
                Vacation vacation= params[0].vacation;
                response = ApiRequest.AddVacation(vacation, params[0].context);
                return response;
            }
            case "AddMemory": {
                Memory memory= params[0].memory;
                Vacation vacation= params[0].vacation;

                response = ApiRequest.AddMemory(memory,vacation, params[0].context);
                return response;
            }
            case "UploadFile": {
                Memory memory= params[0].memory;
                String filePath= params[0].filePath;

                response = ApiRequest.UploadFile(memory, filePath, params[0].context, params[0].fileType);
                return response;
            }
            case "RemoveVacation": {
                Vacation vacation= params[0].vacation;

                response = ApiRequest.RemoveVacation(vacation, params[0].context);
                return response;
            }
            case "DeleteMemory": {
                Memory memory = params[0].memory;
                response = ApiRequest.DeleteMemory(memory,params[0].context);
                return response;
            }
            case "PatchVacation": {
                Vacation vacation= params[0].vacation;

                response = ApiRequest.PatchVacation(vacation, params[0].context);
                return response;
            }
            case "PatchUser": {
                User userToEdit= params[0].user;

                response = ApiRequest.PatchUser(userToEdit, params[0].context);
                return response;
            }
            case "GetUserInfo": {
                User user= params[0].user;

                response = ApiRequest.GetUserInfo(user, params[0].context);
                return response;
            }
            case "DeleteAccount": {
                User user= params[0].user;

                response = ApiRequest.DeleteAccount(user, params[0].context);
                return response;
            }
            case "DeleteMedia": {
                Media media= params[0].media;

                response = ApiRequest.DeleteMedia(media, params[0].context);
                return response;
            }
            case "GetSearchedMemories": {
                User user= params[0].user;
                response = ApiRequest.GetSearchedMemories(user, params[0].context);
                return response;
            }

            default:{
                break;
            }

        }

        return response;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

