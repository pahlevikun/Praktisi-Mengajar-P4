package id.pahlevikun.praktisimengajar.p4.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import id.pahlevikun.praktisimengajar.p4.BaseActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityPermissionBinding;

public class PermissionActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCta();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermissionState();
    }

    private void initPermissionState() {
        binding.tvStatus.setText(String.format("Is Permission Granted >> %s", isAllPermissionGranted()));
        binding.btnRequest.setVisibility(isAllPermissionGranted() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    initPermissionState();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                        showDialogOK((dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                initPermissionState();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                initPermissionState();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Please allow permission from settings", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void initCta() {
        binding.btnRequest.setOnClickListener(view -> {
            String[] permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    PERMISSION_REQUEST_CODE
            );
        });
    }

    private boolean isAllPermissionGranted() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return listPermissionsNeeded.isEmpty();
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Camera or Storage Permission required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}