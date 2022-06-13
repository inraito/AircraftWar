package edu.hitsz.aircraftwar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import dagger.hilt.android.AndroidEntryPoint;
import edu.hitsz.aircraftwar.databinding.MainActivityBinding;
import edu.hitsz.aircraftwar.databinding.PopUpDialogBinding;
import edu.hitsz.aircraftwar.databinding.PopUpLoginBinding;
import edu.hitsz.aircraftwar.game.GameActivity;
import edu.hitsz.aircraftwar.rank.repository.dao.RankEntry;

@AndroidEntryPoint
public class MainMenuActivity extends AppCompatActivity {
    public enum Difficulty implements Serializable {
      EASY,
      NORMAL,
      HARD,
      MULT
    };

    private MainMenuViewModel viewModel;
    private MainActivityBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        this.setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener((view)->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            PopUpLoginBinding dialogBinding = PopUpLoginBinding.inflate(getLayoutInflater());
            dialogBinding.registerButton.setOnClickListener((v)->{
                Toast toast = new Toast(MainMenuActivity.this);
                viewModel.register(dialogBinding.accountInput.getText().toString()
                        , dialogBinding.passwordInput.getText().toString(),
                        toast);
            });
            builder.setView(dialogBinding.getRoot())
                    .setPositiveButton(R.string.login_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast toast = new Toast(MainMenuActivity.this);
                            viewModel.login(dialogBinding.accountInput.getText().toString()
                                    , dialogBinding.passwordInput.getText().toString(),
                                    toast, dialog
                            );
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        });

        binding.multButton.setOnClickListener((view)->{
            if(!viewModel.getLoginLiveData().getValue()){
                return;
            }
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            intent.putExtra(IntentKeys.difficulty, Difficulty.MULT);
            intent.putExtra(IntentKeys.music, binding.musicCheckBox.isChecked());
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setProgress(0);
            dialog.setTitle("搜寻对局中");
            dialog.show();
            viewModel.searchBattle(dialog, this, intent);
        });
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

        viewModel.getLoginLiveData().observe(this, (value)->{
            if(value){
                binding.loginText.setText("已登录:" + viewModel.getAccount());
            }else{
                binding.loginText.setText("未登录");
            }
        });
    }
}
