package edu.hitsz.aircraftwar.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.game.repository.GameRepository;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.MultTemplate;
import edu.hitsz.aircraftwar.game.repository.event.GameoverEvent;
import edu.hitsz.aircraftwar.game.repository.event.TickEvent;
import edu.hitsz.aircraftwar.game.view.BackgroundView;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;
import edu.hitsz.aircraftwar.rank.RankActivity;
import edu.hitsz.aircraftwar.IntentKeys;

@HiltViewModel
public class GameViewModel extends ViewModel {
    private final GameRepository repository;

    @Inject
    public GameViewModel(GameRepository repository){
        this.repository = repository;
    }

    public void registerBackgroundScroll(BackgroundView view) {
        repository.getEventBus().subscribe(TickEvent.class, (event)->{
            view.tick();
        });
    }

    public void registerFlyingObjectView(FlyingObjectView view) {
        repository.getEventBus().subscribe(TickEvent.class, (event)->{
            view.tick();
        });
    }

    public void registerHpText(TextView textView){
        repository.getEventBus().subscribe(TickEvent.class, (event -> {
            textView.post(()->{
               textView.setText("HP:" + repository.getFlyObjectManager().getHeroAircraft().getHp());
            });
        }));
    }

    public void registerScoreText(TextView textView){
        repository.getEventBus().subscribe(TickEvent.class, (event -> {
            textView.post(()->{
                textView.setText("Score:" + repository.getFlyObjectManager().getScore());
            });
        }));
    }

    public void registerGameOver(View view){
        repository.getEventBus().subscribe(GameoverEvent.class, event -> {
            repository.stopGame();
            view.post(()->{
                Intent intent = new Intent(view.getContext(), RankActivity.class);
                intent.putExtra(IntentKeys.score, repository.getFlyObjectManager().getScore());
                intent.putExtra(IntentKeys.difficulty, repository.getDifficulty());
                intent.putExtra(IntentKeys.timeStamp, System.currentTimeMillis());
                view.getContext().startActivity(intent);
            });
        });
    }

    public void registerMultGameOver(View view){
        repository.getEventBus().subscribe(GameoverEvent.class, event -> {
            if(MultTemplate.multGameOver){
                view.post(()->{
                    repository.stopGame();
                    Intent intent = new Intent(view.getContext(), MainMenuActivity.class);
                    Toast toast = new Toast(view.getContext());
                    if(repository.getFlyObjectManager().getScore()>
                    repository.getFlyObjectManager().getRivalScore()){
                        toast.setText("恭喜您，您获胜了");
                    }else{
                        toast.setText("很遗憾，您的对手获胜了");
                    }
                    toast.show();
                    view.getContext().startActivity(intent);
                });
            }
            return;
        });
    }

    public void registerRivalScore(TextView view){
        repository.getEventBus().subscribe(TickEvent.class, (event)->{
            view.post(()->{
                view.setText("Rival:" + repository.getFlyObjectManager().getRivalScore());
            });
        });
    }

    public void bindFlyingObjectManager(FlyingObjectView view){
        view.setManager(repository.getFlyObjectManager());
    }

    public void init(Context context, Rect screenRect, Map<String, Drawable> map, MainMenuActivity.Difficulty difficulty, boolean musicOn){
        repository.init(context, screenRect, map, difficulty, musicOn);
    }

    public void startGame(){
        repository.startGame();
    }

    public void stop(){
        repository.stopGame();
    }
}
