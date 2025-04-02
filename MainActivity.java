package com.firstapp.flashlight;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashlightOn = false;
    private ImageView imageFlashlight;
    private Switch switchFlashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        imageFlashlight = findViewById(R.id.imageFlashlight);
        switchFlashlight = findViewById(R.id.switchFlashlight);

        // Check and request CAMERA permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Get the camera ID
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Handle switch toggle event
        switchFlashlight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                toggleFlashlight(isChecked);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        });
    }

    private void toggleFlashlight(boolean state) {
        try {
            if (state) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashlightOn = true;
                imageFlashlight.setImageResource(R.drawable.flashlight_on); // Change image to ON state
                switchFlashlight.setChecked(true);
                Toast.makeText(this, "Flashlight ON", Toast.LENGTH_SHORT).show();
            } else {
                cameraManager.setTorchMode(cameraId, false);
                isFlashlightOn = false;
                imageFlashlight.setImageResource(R.drawable.flashlight_off); // Change image to OFF state
                switchFlashlight.setChecked(false);
                Toast.makeText(this, "Flashlight OFF", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
