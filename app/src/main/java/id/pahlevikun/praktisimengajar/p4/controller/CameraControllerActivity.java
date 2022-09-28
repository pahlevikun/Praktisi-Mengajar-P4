package id.pahlevikun.praktisimengajar.p4.controller;

import com.google.common.util.concurrent.ListenableFuture;

import android.os.Bundle;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.LifecycleCameraController;
import androidx.core.content.ContextCompat;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityCameraControllerBinding;

public class CameraControllerActivity extends AppCompatActivity {

    private ActivityCameraControllerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCameraX();
        initController();
    }

    private void initCameraX() {
        binding.viewFinder.post(new Runnable() {
            @Override
            public void run() {
                ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(CameraControllerActivity.this);
                cameraProviderFuture.addListener((Runnable) () -> {
                    ProcessCameraProvider cameraProvider = null;
                    try {
                        cameraProvider = cameraProviderFuture.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    Preview preview = new Preview
                            .Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .setTargetRotation(binding.viewFinder.getDisplay().getRotation())
                            .build();
                    preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(CameraControllerActivity.this, CameraSelector.DEFAULT_BACK_CAMERA, preview);
                }, ContextCompat.getMainExecutor(CameraControllerActivity.this));
            }
        });
    }

    private void initController() {
        LifecycleCameraController controller = new LifecycleCameraController(this);
        controller.bindToLifecycle(this);
        controller.setTapToFocusEnabled(true);
        controller.setPinchToZoomEnabled(true);
        binding.viewFinder.setController(controller);
    }
}