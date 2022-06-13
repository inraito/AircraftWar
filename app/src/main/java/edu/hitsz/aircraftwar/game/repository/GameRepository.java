package edu.hitsz.aircraftwar.game.repository;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.MainMenuActivity;
import edu.hitsz.aircraftwar.game.repository.difficulty.TemplateManager;
import edu.hitsz.aircraftwar.game.repository.event.TickEvent;
import edu.hitsz.aircraftwar.game.view.FlyingObjectView;

@Singleton
public class GameRepository {
    //time per tick
    private Rect screenRect;
    private Map<String, Drawable> imageMap;
    public volatile static boolean gameOverFlag = false;

    private void tick(){
        eventBus.publish(new TickEvent());
    }

    private Context context;
    private boolean musicOn;
    private MainMenuActivity.Difficulty difficulty;
    private final GameEventBus eventBus;
    private final GameAssetsManager manager;
    private final TemplateManager templateManager;
    private final GameAudioManager audioManager;
    private ScheduledExecutorService executorService;

    @Inject
    public GameRepository(GameEventBus eventBus, GameAssetsManager objectManager, TemplateManager templateManager, GameAudioManager audioManager){
        this.eventBus = eventBus;
        this.manager = objectManager;
        this.templateManager = templateManager;
        this.audioManager = audioManager;
        preInit();
    }

    public GameEventBus getEventBus(){
        return this.eventBus;
    }

    private void preInit(){

    }

    /**
     * pass the runtime argument about ui to repository
     * @param screenRect, size of the Game view
     * @param map, all drawables, keys in
     * @see FlyingObjectView.FlyingObjectKeys
     */
    public void init(Context context, Rect screenRect, Map<String, Drawable> map, MainMenuActivity.Difficulty difficulty, boolean musicOn){
        this.context = context;
        this.difficulty = difficulty;
        this.screenRect = screenRect;
        this.imageMap = map;
        this.audioManager.init(context);
        this.musicOn = musicOn;
        //
        if(musicOn){
            audioManager.playNormalBgm();
        }

        FlyingObjectFactoryManager.init(map, screenRect);
        this.executorService = new ScheduledThreadPoolExecutor(1);
        eventBus.flush();
        manager.flush();


        switch (difficulty){
            case EASY:{
                templateManager.getEasyTemplate().init(musicOn);
                break;
            }
            case HARD:{
                templateManager.getHardTemplate().init(musicOn);
                break;
            }
            case NORMAL:{
                templateManager.getNormalTemplate().init(musicOn);
                break;
            }
            case MULT:{
                templateManager.getMultTemplate().init(musicOn);
                break;
            }
        }
        manager.getHeroAircraft();
    }

    public void startGame(){
        executorService.scheduleWithFixedDelay(this::tick,
                StaticField.timeInterval,
                StaticField.timeInterval,
                TimeUnit.MILLISECONDS);
    }

    public MainMenuActivity.Difficulty getDifficulty(){
        return this.difficulty;
    }

    public GameAssetsManager getFlyObjectManager(){
        return this.manager;
    }

    public Rect getScreenRect(){
        return this.screenRect;
    }

    public void stopGame(){
        executorService.shutdown();
        audioManager.shutdown();
    }
}
