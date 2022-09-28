package id.pahlevikun.praktisimengajar.p4.saveImage;

import com.google.common.util.concurrent.ListenableFuture;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.LifecycleCameraController;
import androidx.core.content.ContextCompat;
import id.pahlevikun.praktisimengajar.p4.BaseActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityCameraSaveImageBinding;
import id.pahlevikun.praktisimengajar.p4.helper.ImageHelper;
import id.pahlevikun.praktisimengajar.p4.preview.PreviewActivity;

public class CameraSaveImageActivity extends BaseActivity {

    private ActivityCameraSaveImageBinding binding;
    private LifecycleCameraController controller;
    private ExecutorService cameraExecution;
    private ImageCapture imageCapture;
    private ImageHelper.ImageData imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraSaveImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCameraX();
    }

    private void initCameraX() {
        controller = new LifecycleCameraController(this);
        controller.bindToLifecycle(this);
        controller.setPinchToZoomEnabled(true);
        controller.setTapToFocusEnabled(true);
        imageCapture = new ImageCapture
                .Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        binding.cameraCaptureButton.setOnClickListener(view -> {
            cameraExecution = Executors.newSingleThreadExecutor();
            imageData = ImageHelper.createImageFile(CameraSaveImageActivity.this, ImageHelper.StorageType.STORAGE);
            ImageCapture.OutputFileOptions outputFile = new ImageCapture.OutputFileOptions
                    .Builder(imageData.getImageFile())
                    .setMetadata(new ImageCapture.Metadata())
                    .build();
            imageCapture.takePicture(
                    outputFile,
                    cameraExecution,
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Intent intent = new Intent(CameraSaveImageActivity.this, PreviewActivity.class);
                            intent.putExtra(PreviewActivity.EXTRA_KEY, imageData);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            exception.getImageCaptureError();
                        }
                    }
            );
        });

        binding.viewFinder.setController(controller);
        binding.viewFinder.post(new Runnable() {
            @Override
            public void run() {
                ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(CameraSaveImageActivity.this);
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
                    cameraProvider.bindToLifecycle(
                            CameraSaveImageActivity.this,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                    );
                }, ContextCompat.getMainExecutor(CameraSaveImageActivity.this));
            }
        });
    }
}