package edu.hitsz.aircraftwar.rank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import edu.hitsz.aircraftwar.IntentKeys;
import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.R;
import edu.hitsz.aircraftwar.databinding.PopUpDialogBinding;
import edu.hitsz.aircraftwar.databinding.RankActivityBinding;
import edu.hitsz.aircraftwar.rank.repository.dao.RankEntry;

@AndroidEntryPoint
public class RankActivity extends AppCompatActivity {
    private RankViewModel viewModel;
    private RankActivityBinding binding;

    @Inject
    RecyclerAdaptor adaptor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Intent intent = getIntent();
        int score = intent.getIntExtra(IntentKeys.score, -1);
        MainMenuActivity.Difficulty difficulty = (MainMenuActivity.Difficulty) intent.getSerializableExtra(IntentKeys.difficulty);
        long timeStamp = intent.getLongExtra(IntentKeys.timeStamp, 0);

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RankViewModel.class);
        binding = RankActivityBinding.inflate(getLayoutInflater());
        binding.recycler.setAdapter(this.adaptor);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        //delete record
        binding.deleteButton.setOnClickListener((view)->{
            List<RankEntry> list = new ArrayList<>();
            for (int i = 0; i < binding.recycler.getChildCount(); i++) {
                CheckBox checkBox = binding.recycler.getChildAt(i).findViewById(R.id.deleteCheckBox);
                if (checkBox.isChecked()) {
                    list.add(adaptor.getRankEntryAt(i));
                }
            }
            viewModel.DeleteRankEntry(list);
        });
        this.setContentView(binding.getRoot());

        viewModel.getRankEntryLiveData().observe(this, rankEntries -> {
            adaptor.update(rankEntries);
            binding.recycler.invalidate();
        });
        viewModel.init(this, difficulty);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        PopUpDialogBinding dialogBinding = PopUpDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot())
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Editable text = dialogBinding.userNameInput.getText();
                                if(text==null){
                                    return;
                                }
                                RankEntry entry = new RankEntry(timeStamp, score, text.toString(), difficulty);
                                viewModel.InsertRankEntry(entry);
                                dialog.dismiss();
                            }
                        })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
        builder.create().show();
    }
}
