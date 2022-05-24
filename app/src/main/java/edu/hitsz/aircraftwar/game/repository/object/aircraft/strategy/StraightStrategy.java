package edu.hitsz.aircraftwar.game.repository.object.aircraft.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hitsz.aircraftwar.game.repository.FlyingObjectFactoryManager;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.AbstractAircraft;
import edu.hitsz.aircraftwar.game.repository.object.aircraft.HeroAircraft;
import edu.hitsz.aircraftwar.game.repository.object.bullet.AbstractBullet;
import edu.hitsz.aircraftwar.game.repository.object.bullet.EnemyBullet;
import edu.hitsz.aircraftwar.game.repository.object.bullet.HeroBullet;

public class StraightStrategy implements ShootStrategy{
    private final int direction;
    private int shootNum;
    private int power;

    public void setShootNum(int shootNum) {
        if(shootNum>=1) {
            this.shootNum = shootNum;
        }
    }

    public void setPower(int power) {
        if(power>0){
            this.power = power;
        }
    }

    public StraightStrategy(int direction,int shootNum, int power){
        this.direction = direction;
        this.shootNum = shootNum;
        this.power = power;
    }

    @Override
    public List<AbstractBullet> shoot(AbstractAircraft context) {
        List<AbstractBullet> res = Collections.synchronizedList(new ArrayList<>());
        int x = context.getLocationX();
        int y = context.getLocationY() + this.direction*2;
        int speedX = 0;
        int speedY = context.getSpeedY() + direction*10;
        AbstractBullet baseBullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if(context instanceof HeroAircraft) {
                baseBullet = FlyingObjectFactoryManager.getBulletFactory().getHeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            }
            else{
                baseBullet = FlyingObjectFactoryManager.getBulletFactory().getEnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            }
            res.add(baseBullet);
        }
        return res;
    }
}
