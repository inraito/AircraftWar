package edu.hitsz.aircraftwar;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;

import dagger.hilt.android.AndroidEntryPoint;
import edu.hitsz.aircraftwar.databinding.MainActivityBinding;
import edu.hitsz.aircraftwar.game.GameActivity;

@AndroidEntryPoint
public class MainMenuActivity extends AppCompatActivity {
    public enum Difficulty implements Serializable {
      EASY,
      NORMAL,
      HARD
    };

    private MainMenuViewModel viewModel;
    private MainActivityBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        this.setContentView(binding.getRoot());

        binding.easyButton.setOnClickListener((view)->{
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra(IntentKeys.difficulty, Difficulty.EASY);
            intent.putExtra(IntentKeys.music, binding.musicCheckBox.isChecked());
            startActivity(intent);
        });
        binding.normalButton.setOnClickListener((view)->{
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra(IntentKeys.difficulty, Difficulty.NORMAL);
            intent.putExtra(IntentKeys.music, binding.musicCheckBox.isChecked());
            startActivity(intent);
        });
        binding.hardButton.setOnClickListener((view)->{
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra(IntentKeys.difficulty, Difficulty.HARD);
            intent.putExtra(IntentKeys.music, binding.musicCheckBox.isChecked());
            startActivity(intent);
        });

    }
}
