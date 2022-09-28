package id.pahlevikun.praktisimengajar.p4.preview;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import id.pahlevikun.praktisimengajar.p4.BaseActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityPreviewBinding;
import id.pahlevikun.praktisimengajar.p4.helper.ImageHelper;

public class PreviewActivity extends BaseActivity {

    private ActivityPreviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initPhotoView();
    }

    private void initPhotoView() {
        ImageHelper.ImageData imageData = (ImageHelper.ImageData) getIntent().getSerializableExtra(EXTRA_KEY);
        if (imageData == null) finish();
        Glide.with(this)
                .load(imageData.getImagePath())
                .fitCenter()
                .into(binding.photoView);
    }

    public static final String EXTRA_KEY = "intent.extra.key";
}