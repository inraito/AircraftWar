package edu.hitsz.aircraftwar.game.repository;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.game.repository.event.BaseEvent;

/**
 * static eventBus, subscribing or unsubscribing after start of the game is not allowed
 */
@Singleton
public class GameEventBus {
    public interface GameEventSubscriber<T extends BaseEvent>{
        public void onEvent(T event);
    }

    private final HashMap<Class<?>, HashSet<GameEventSubscriber<?>>> subscribers = new HashMap<>();

    @Inject
    GameEventBus(){

    }

    //repository is responsible for calling this when game restarting
    public void flush(){
        subscribers.clear();
    }

    public <T extends BaseEvent> void subscribe(Class<T> c, GameEventSubscriber<T> subscriber){
        if(!BaseEvent.class.isAssignableFrom(c)){
            throw new RuntimeException("Subscriber type invalid");
        }
        HashSet<GameEventSubscriber<?>> subSubscribers = subscribers.get(c);
        if(subSubscribers == null){
            subSubscribers = new HashSet<>();
            subSubscribers.add(subscriber);
            subscribers.put(c, subSubscribers);
        }else{
            subSubscribers.add(subscriber);
        }
    }

    public void publish(BaseEvent event){
        HashSet<GameEventSubscriber<?>> subSubscribers = subscribers.get(event.getClass());
        if(subSubscribers == null){
            return;
        }
        for(GameEventSubscriber subscriber: subSubscribers){
            try {
                subscriber.onEvent(event);
            }catch (ClassCastException e){
                Log.e("GameEventBus", "Event dispatch failure!");
                e.printStackTrace();
            }
        }
    }
}
