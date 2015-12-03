package group9.android_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Halling- on 2015-12-03.
 */
public class CameraActivity extends AppCompatActivity {
    Button btnTakePhoto;
    ImageView imgTakenPhoto;
    private static final int CAM_REQUEST = 1313;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.camera_layout);

        btnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
        imgTakenPhoto = (ImageView)findViewById(R.id.ivThumb);

        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());

    }



    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAM_REQUEST)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgTakenPhoto.setImageBitmap(thumbnail);
        }
    }

    class btnTakePhotoClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }
    }

}
