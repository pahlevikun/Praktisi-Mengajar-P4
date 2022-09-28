package id.pahlevikun.praktisimengajar.p4.camerax;

import com.google.common.util.concurrent.ListenableFuture;

import android.os.Bundle;

import java.util.concurrent.ExecutionException;

import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import id.pahlevikun.praktisimengajar.p4.BaseActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityCameraXBinding;

public class CameraXActivity extends BaseActivity {

    private ActivityCameraXBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraXBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCameraX();
    }

    private void initCameraX() {
        binding.viewFinder.post(new Runnable() {
            @Override
            public void run() {
                ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(CameraXActivity.this);
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
                    cameraProvider.bindToLifecycle(CameraXActivity.this, CameraSelector.DEFAULT_BACK_CAMERA, preview);
                }, ContextCompat.getMainExecutor(CameraXActivity.this));
            }
        });
    }
}