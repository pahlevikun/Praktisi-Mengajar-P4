package id.pahlevikun.praktisimengajar.p4.builder;

import androidx.appcompat.app.AppCompatActivity;
import id.pahlevikun.praktisimengajar.p4.databinding.ActivityBuilderAndSingletonBinding;

import android.os.Bundle;

public class BuilderAndSingletonActivity extends AppCompatActivity {

    private ActivityBuilderAndSingletonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuilderAndSingletonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvSimpleSingleton.setText(String.format("Simple singleton count >> %d", SimpleSingleton.getInstance().getIntentCount()));
        binding.tvBuilderSingleton.setText(String.format("Builder singleton permission count >> %d", BuilderSimpleSingleton.getInstance().getPermissionCount()));
    }
}