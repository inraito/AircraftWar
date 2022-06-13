package edu.hitsz.aircraftwar.game;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import edu.hitsz.aircraftwar.IntentKeys;
import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.databinding.GameActivityBinding;
import edu.hitsz.aircraftwar.util.Assert;

@AndroidEntryPoint
public class GameActivity extends AppCompatActivity {
    private GameViewModel viewModel;
    private GameActivityBinding binding;
    private MainMenuActivity.Difficulty difficulty = MainMenuActivity.Difficulty.EASY;
    private boolean musicOn = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        binding = GameActivityBinding.inflate(getLayoutInflater());
        this.setContentView(binding.getRoot());

        //get extras from intent
        try {
            difficulty = (MainMenuActivity.Difficulty) getIntent().getSerializableExtra(IntentKeys.difficulty);
            musicOn = getIntent().getBooleanExtra(IntentKeys.music, false);
            Assert.assertNotNull(difficulty);
        }catch (ClassCastException|NullPointerException e){
            Log.e("assertion", "assertion failed when getting extras from MainMenuActivity");
            Log.e("assertion", e.getStackTrace().toString());
            this.finish();
        }
        binding.getRoot().post(()->{
            Rect screenRect = new Rect();
            binding.getRoot().getDrawingRect(screenRect);
            viewModel.init(this, screenRect, binding.flyingObjectView.getDrawableMap(), difficulty, musicOn);

            viewModel.registerBackgroundScroll(binding.backgroundView);
            viewModel.registerFlyingObjectView(binding.flyingObjectView);
            viewModel.registerHpText(binding.hpText);
            viewModel.registerScoreText(binding.scoreText);
            if(difficulty== MainMenuActivity.Difficulty.MULT){
                viewModel.registerMultGameOver(binding.getRoot());
                viewModel.registerRivalScore(binding.rivalScore);
                binding.rivalScore.setVisibility(View.VISIBLE);
            }else {
                viewModel.registerGameOver(binding.getRoot());
            }
            viewModel.bindFlyingObjectManager(binding.flyingObjectView);
            viewModel.startGame();
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStop(){
        Log.d("GameActivity", "onStop() Called!");
        super.onStop();
        viewModel.stop();
    }
}
