package group9.android_project;
import com.loopj.android.http.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Halling- on 2015-12-04.
 */
public class Upload {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(int url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postVideo(int memoryId, File file, String token, AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("Authorization", "bearer " + token);
        RequestParams params = new RequestParams();
        try {
            params.put("video-file", file);
        } catch(FileNotFoundException e) {}

        client.post(getAbsoluteUrl(memoryId), params, responseHandler);
    }
    public static void postPicture(int memoryId,File file, String token, AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("Authorization", "bearer " + token);
        RequestParams params = new RequestParams();
        try {
            params.put("picture-file", file);
        } catch(FileNotFoundException e) {}

        client.post(getAbsoluteUrlPicture(memoryId), params, responseHandler);
    }

    private static String getAbsoluteUrl(int memoryId) {
        final String BASE_URL = "http://www.abs-cloud.elasticbeanstalk.com/api/v1/memories/"+memoryId+"/videos";
        return BASE_URL;
    }
    private static String getAbsoluteUrlPicture(int memoryId) {
        final String BASE_URL = "http://www.abs-cloud.elasticbeanstalk.com/api/v1/memories/"+memoryId+"/pictures?width=26&height=26";//&videocodec=video&videobitrate=26&framerate=26&audiocodec=video&audiobitrate=26&channels=26&samplingrate=26";
        return BASE_URL;
    }

}
