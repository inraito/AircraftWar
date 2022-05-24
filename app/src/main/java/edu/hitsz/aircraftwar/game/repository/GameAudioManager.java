package edu.hitsz.aircraftwar.game.repository;

import static edu.hitsz.aircraftwar.game.repository.GameAudioManager.audioKeys.*;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.R;

@Singleton
public class GameAudioManager {
    private Context context;
    private SoundPool notificationSoundPool;
    private MediaPlayer bgmPlayer;

    private HashMap<String, Integer> map;

    @Inject
    public GameAudioManager(){

    }

    public void init(Context context){
        this.context = context;
        AudioAttributes notificationAttributes = (new AudioAttributes.Builder()).
                setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        notificationSoundPool = (new SoundPool.Builder()).setMaxStreams(2).setAudioAttributes(notificationAttributes).build();

        map = new HashMap<>();
        map.put(shoot, notificationSoundPool.load(context, R.raw.bullet, 1));
        map.put(bulletHit, notificationSoundPool.load(context, R.raw.bullet_hit, 1));
        map.put(bombExplode, notificationSoundPool.load(context, R.raw.bomb_explosion, 1));
        map.put(gameOver, notificationSoundPool.load(context, R.raw.game_over, 1));
        map.put(supplyGet, notificationSoundPool.load(context, R.raw.get_supply, 1));
    }

    public void stopBgm(){
        if(bgmPlayer==null){
            return;
        }
        bgmPlayer.stop();
        bgmPlayer.reset();
        bgmPlayer.release();
    }

    public void shutdown(){
        stopBgm();
        bgmPlayer = null;
    }

    public void playNormalBgm(){
        if(bgmPlayer != null) {
            stopBgm();
        }
        bgmPlayer = MediaPlayer.create(context, R.raw.bgm);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();
    }

    public void playBossBgm(){
        stopBgm();
        bgmPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();
    }

    public void playShootSound(){
        notificationSoundPool.play(map.get(shoot), 1, 1, 0, 0, 1);
    }

    public void playBulletHitSound(){
        notificationSoundPool.play(map.get(bulletHit), 1, 1, 0, 0, 1);
    }

    public void playBombExplodeSound(){
        notificationSoundPool.play(map.get(bombExplode), 1, 1, 0, 0, 1);
    }

    public void playGameOverSound(){
        notificationSoundPool.play(map.get(gameOver), 1, 1, 0, 0, 1);
    }

    public void playSupplyGetSound(){
        notificationSoundPool.play(map.get(supplyGet), 1, 1, 0, 0, 1);
    }

    public static class audioKeys{
        public static String shoot = "Shoot";
        public static String bulletHit = "BulletHit";
        public static String bombExplode = "BombExplode";
        public static String gameOver = "GameOver";
        public static String supplyGet = "SupplyGet";
    }

}
