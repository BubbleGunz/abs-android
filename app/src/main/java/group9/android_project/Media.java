package group9.android_project;

import android.graphics.Bitmap;

/**
 * Created by Halling- on 2015-11-20.
 */
public class Media {
    public int id;
    public String fileURL,container;
    public Bitmap bitmap;

}
class PictureMedia extends Media {
    public int width,height;
}
class VideoMedia extends Media {
    public int width,height,channels;
    public float videoBitrate,framerate,audioBitrate,samplingrate;
    public String videoCodec,audioCodec;
}
