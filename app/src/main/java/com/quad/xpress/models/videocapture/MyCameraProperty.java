package com.quad.xpress.models.videocapture;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * Created by Venkatesh on 12-05-16.
 */
public class MyCameraProperty {

    public boolean FindFrontCamera(Context myContext, Camera mCamera) {
        if (!hasCamera(myContext)) {
           // Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
         //   toast.show();
            return false;
        }

        if (mCamera!= null) {
            // if the front facing camera does not exist
           // Toast.makeText(myContext, "findFrontFacingCamera "+ findFrontFacingCamera(), Toast.LENGTH_LONG).show();

            if (findFrontFacingCamera() == 1) {
                // release the old camera instance
                // switch camera, from the front and the back and vice versa
                //releaseCamera();
                // chooseCamera();
               // Toast.makeText(myContext, "findFrontFacingCamera", Toast.LENGTH_LONG).show();
                return true;

            } else {
              //  Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
             //   toast.show();
                return false;
            }
        }
        return false;
    }

    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

}
