package com.kevalpatel2106.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.CameraFacing;
import com.androidhiddencamera.HiddenCameraFragment;

/**
 * Created by Keval on 11-Nov-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class HiddenCamFragment extends HiddenCameraFragment {

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hidden_cam, container, false);

        //Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            //Start camera preview
            startCamera(CameraFacing.FRONT_FACING_CAMERA);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        mImageView = (ImageView) view.findViewById(R.id.cam_prev);

        //Take a picture
        view.findViewById(R.id.capture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take picture using the camera without preview.
                takePicture();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //noinspection MissingPermission
                startCamera(CameraFacing.REAR_FACING_CAMERA);
            } else {
                Toast.makeText(getActivity(), "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onImageCapture(@NonNull Bitmap image) {

        //Display the image to the image view
        mImageView.setImageBitmap(image);
    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(getActivity(), "Cannot open camera.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camra permission before initializing it.
                Toast.makeText(getActivity(), "Camera permission not available.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_TAKE_IMAGE_FAILED:
                //Take picture failed.
                Toast.makeText(getActivity(), "Cannot capture image.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
