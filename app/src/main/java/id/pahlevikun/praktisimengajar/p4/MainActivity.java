package id.pahlevikun.praktisimengajar.p4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import id.pahlevikun.praktisimengajar.p4.builder.BuilderAndSingletonActivity;
import id.pahlevikun.praktisimengajar.p4.builder.BuilderSimpleSingleton;
import id.pahlevikun.praktisimengajar.p4.builder.SimpleSingleton;
import id.pahlevikun.praktisimengajar.p4.camerax.CameraXActivity;
import id.pahlevikun.praktisimengajar.p4.controller.CameraControllerActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityMainBinding;
import id.pahlevikun.praktisimengajar.p4.permission.PermissionActivity;
import id.pahlevikun.praktisimengajar.p4.saveImage.CameraSaveImageActivity;
import id.pahlevikun.praktisimengajar.p4.tensorflow.CameraActivity;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private SimpleSingleton simpleSingleton = SimpleSingleton.getInstance();
    private BuilderSimpleSingleton builderSimpleSingleton = new BuilderSimpleSingleton
            .Builder()
            .setInitialCount(3)
            .init();

    ActivityResultLauncher<Intent> takePictureResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Bitmap captured = (Bitmap) data.getExtras().get("data");
                        binding.imageView.setImageBitmap(captured);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvSimpleSingleton.setText(String.format("Simple singleton intent count %d", simpleSingleton.getIntentCount()));
    }

    private void intViews() {
        initCta(binding.btnPermission, PermissionActivity.class);
        initCta(binding.btnCameraX, CameraXActivity.class);
        initCta(binding.btnTensorFlow, CameraActivity.class);
        initCta(binding.btnCameraSaveImage, CameraSaveImageActivity.class);
        initCta(binding.btnCameraController, CameraControllerActivity.class);
        initCta(binding.btnBuilderSingleton, BuilderAndSingletonActivity.class);

        binding.btnTakePicture.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureResult.launch(takePictureIntent);
        });
    }

    private <T> void initCta(View view, Class<T> target) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleSingleton.addIntent();
                if (target == PermissionActivity.class) {
                    builderSimpleSingleton.countIntentToPermission();
                }
                startActivity(new Intent(MainActivity.this, target));
            }
        });
    }
}