package edu.hitsz.aircraftwar.game.repository.difficulty.template;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.GameURL;
import edu.hitsz.aircraftwar.game.repository.GameAssetsManager;
import edu.hitsz.aircraftwar.game.repository.GameAudioManager;
import edu.hitsz.aircraftwar.game.repository.GameEventBus;
import edu.hitsz.aircraftwar.game.repository.event.GameoverEvent;
import edu.hitsz.aircraftwar.game.repository.event.TickEvent;

@Singleton
public class MultTemplate extends NormalTemplate{
    //TODO:refactor
    public static String account;
    public static String password;

    @Inject
    public MultTemplate(GameAssetsManager manager, GameEventBus eventBus, GameAudioManager audioManager) {
        super(manager, eventBus, audioManager);
    }

    @Override
    public void init(boolean musicOn){
        super.init(musicOn);
        eventBus.subscribe(TickEvent.class, syncScoreSubscriber);
        eventBus.subscribe(GameoverEvent.class, statusChangeSubscriber);
        this.status = "ongoing";
        multGameOver = false;
    }

    private volatile String status;
    //TODO:refactor
    public static volatile boolean multGameOver;
    public final GameEventBus.GameEventSubscriber<TickEvent> syncScoreSubscriber = new GameEventBus.GameEventSubscriber<TickEvent>() {
        private int count = 0;
        @Override
        public void onEvent(TickEvent event) {
            if(multGameOver){
                eventBus.publish(new GameoverEvent());
                return;
            }
            if(count>=25){
                count = count % 50;
                new Thread(()->{
                    try {
                        URL url = new URL(GameURL.update);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        JSONObject object = new JSONObject();
                        object.put("account", account);
                        object.put("password", password);
                        object.put("score", manager.getScore());
                        object.put("status", status);
                        OutputStream stream = connection.getOutputStream();
                        OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                        writer.write(object.toString());
                        writer.flush();
                        writer.close();
                        stream.close();
                        connection.connect();
                        char[] buffer = new char[100];
                        int num = new InputStreamReader(connection.getInputStream()).read(buffer);
                        System.out.println(String.valueOf(buffer, 0, num));
                        JSONObject response = new JSONObject(String.valueOf(buffer, 0, num));
                        manager.setRivalScore(response.getInt("score"));
                        if(response.getString("status").equals("ongoing")){

                        }else if(response.getString("status").equals("closing")){
                            MultTemplate.this.status = "closing";
                        }else if(response.getString("status").equals("closed")){
                            if(status.equals("closed")){
                                multGameOver = true;
                            }else {
                                status = "closed";
                            }
                        }else{
                            multGameOver = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException e){
                        multGameOver = true;
                    }
                }).start();
            }else{
                count++;
            }
        }
    };

    public final GameEventBus.GameEventSubscriber<GameoverEvent> statusChangeSubscriber = new GameEventBus.GameEventSubscriber<GameoverEvent>() {
        @Override
        public void onEvent(GameoverEvent event) {
            if(multGameOver) {
                return;
            }else {
                status = "closing";
            }
        }
    };
}
