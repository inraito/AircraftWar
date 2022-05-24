package edu.hitsz.aircraftwar.game.view;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.hitsz.aircraftwar.R;
import edu.hitsz.aircraftwar.game.repository.GameAssetsManager;
import edu.hitsz.aircraftwar.game.repository.object.AbstractFlyingObject;

import static edu.hitsz.aircraftwar.game.view.FlyingObjectView.FlyingObjectKeys.*;

public class FlyingObjectView extends View {
    private GameAssetsManager manager = null;

    Map<String, Drawable> drawableMap = new HashMap<>();
    Drawable mMobAircraftImage;
    Drawable mEliteAircraftImage;
    Drawable mBossAircraftImage;
    Drawable mHeroAircraftImage;
    Drawable mBloodPropImage;
    Drawable mBombPropImage;
    Drawable mBulletPropImage;
    Drawable mEnemyBulletImage;
    Drawable mHeroBulletImage;

    public FlyingObjectView(Context context) {
        super(context);
    }

    public void setManager(GameAssetsManager manager){
        this.manager = manager;
    }

    public Map<String, Drawable> getDrawableMap(){
        return drawableMap;
    }

    public FlyingObjectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FlyingObjectView,
                0, 0);
        try {
            mMobAircraftImage = a.getDrawable(R.styleable.FlyingObjectView_mob_image);
            mEliteAircraftImage = a.getDrawable(R.styleable.FlyingObjectView_elite_image);
            mBossAircraftImage = a.getDrawable(R.styleable.FlyingObjectView_boss_image);
            mHeroAircraftImage = a.getDrawable(R.styleable.FlyingObjectView_hero_image);
            mBloodPropImage = a.getDrawable(R.styleable.FlyingObjectView_blood_prop_image);
            mBombPropImage = a.getDrawable(R.styleable.FlyingObjectView_bomb_prop_image);
            mBulletPropImage = a.getDrawable(R.styleable.FlyingObjectView_bullet_prop_image);
            mEnemyBulletImage = a.getDrawable(R.styleable.FlyingObjectView_enemy_bullet_image);
            mHeroBulletImage = a.getDrawable(R.styleable.FlyingObjectView_hero_bullet_image);
        } finally {
            a.recycle();
        }
        drawableMap.put(mobImage, mMobAircraftImage);
        drawableMap.put(eliteImage, mEliteAircraftImage);
        drawableMap.put(bossImage, mBossAircraftImage);
        drawableMap.put(heroImage, mHeroAircraftImage);
        drawableMap.put(bloodPropImage, mBloodPropImage);
        drawableMap.put(bombImage, mBombPropImage);
        drawableMap.put(bulletPropImage, mBulletPropImage);
        drawableMap.put(enemyBulletImage, mEnemyBulletImage);
        drawableMap.put(heroBulletImage, mHeroBulletImage);
    }

    private void drawFlyingObject(AbstractFlyingObject flyingObject, Canvas canvas){
        Drawable drawable = drawableMap.get(flyingObject.getImageKey());
        drawable.setBounds(
                flyingObject.getLocationX()-flyingObject.getWidth()/2,
                flyingObject.getLocationY() - flyingObject.getHeight()/2,
                flyingObject.getLocationX() + flyingObject.getWidth()/2,
                flyingObject.getLocationY() + flyingObject.getHeight()/2
        );
        drawable.draw(canvas);
    }

    private void drawFlyingObjectList(List<? extends AbstractFlyingObject> list, Canvas canvas){
        synchronized (list){
            for(AbstractFlyingObject flyingObject:list){
                drawFlyingObject(flyingObject, canvas);
            }
        }
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(manager==null){
            return;
        }
        drawFlyingObjectList(manager.getHeroBullets(), canvas);
        drawFlyingObjectList(manager.getEnemyBullets(), canvas);
        drawFlyingObjectList(manager.getEnemyAircraft(), canvas);
        drawFlyingObjectList(manager.getProps(), canvas);
        drawFlyingObject(manager.getHeroAircraft(), canvas);
    }

    public void tick(){
        this.invalidate();
    }

    public static class FlyingObjectKeys {
        public static String mobImage = "MobImage";
        public static String eliteImage = "EliteImage";
        public static String bossImage = "BossImage";
        public static String heroImage = "HeroImage";
        public static String bloodPropImage = "BloodPropImage";
        public static String bombImage = "BombImage";
        public static String bulletPropImage = "BulletPropImage";
        public static String enemyBulletImage = "EnemyBulletImage";
        public static String heroBulletImage = "HeroBulletImage";
    }

    //handling players interaction
    private int pointerID;
    private boolean IDValid = false;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int index = event.getActionIndex();
        if(!IDValid){
            if(action == ACTION_DOWN||action == ACTION_POINTER_DOWN){
                if(manager.getHeroAircraft().isInside(event.getX(index), event.getY(index))){
                    pointerID = event.getPointerId(event.getActionMasked());
                    IDValid = true;
                    Log.d("FlyingObjectView", "Touch point inside hero");
                }
            }
            return true;
        }else{
            if(event.getPointerId(index)==pointerID &&
                    (action == ACTION_UP||action == ACTION_CANCEL|| action == ACTION_POINTER_UP)){
                IDValid = false;
            }
            int pointerIndex = event.findPointerIndex(pointerID);
            manager.getHeroAircraft().setLocation((int)event.getX(pointerIndex),
                    (int)event.getY(pointerIndex));
        }
        return true;
    }
}
