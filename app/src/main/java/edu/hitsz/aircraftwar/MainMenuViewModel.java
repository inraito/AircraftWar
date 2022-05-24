package edu.hitsz.aircraftwar;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.hitsz.aircraftwar.game.GameActivity;

@HiltViewModel
public class MainMenuViewModel extends ViewModel {

    @Inject
    public MainMenuViewModel(){

    }
}
