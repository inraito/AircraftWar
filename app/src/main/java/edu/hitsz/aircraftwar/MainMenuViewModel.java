package edu.hitsz.aircraftwar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.hitsz.aircraftwar.game.repository.StaticField;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.MultTemplate;

@HiltViewModel
public class MainMenuViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLogin = new MutableLiveData<>();
    private String account;
    private String password;

    @Inject
    public MainMenuViewModel(){
        isLogin.setValue(false);
    }

    public LiveData<Boolean> getLoginLiveData(){
        return this.isLogin;
    }

    public void searchBattle(ProgressDialog dialog, MainMenuActivity activity, Intent intent){
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleWithFixedDelay(()->{
                    try {
                        URL url = new URL(GameURL.search);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        JSONObject object = new JSONObject();
                        object.put("account", account);
                        object.put("password", password);
                        OutputStream stream = connection.getOutputStream();
                        OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                        writer.write(object.toString());
                        writer.flush();
                        writer.close();
                        stream.close();
                        connection.connect();
                        char[] buffer = new char[100];
                        new InputStreamReader(connection.getInputStream()).read(buffer);
                        JSONObject response = new JSONObject(String.valueOf(buffer));
                        if(response.getString("result").equals("matched")){
                            dialog.dismiss();
                            activity.startActivity(intent);
                            executorService.shutdown();
                            //TODO:refactor
                            MultTemplate.account = account;
                            MultTemplate.password = password;
                        }else{
                            return;
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                },
                0,
                1000,
                TimeUnit.MILLISECONDS);
    }

    public void login(String account, String password, Toast toast, DialogInterface dialog){
        new Thread(()->{
            try {
                URL url = new URL(GameURL.login);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                JSONObject object = new JSONObject();
                object.put("account", account);
                object.put("password", password);
                OutputStream stream = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                writer.write(object.toString());
                writer.flush();
                writer.close();
                stream.close();
                connection.connect();
                char[] buffer = new char[100];
                new InputStreamReader(connection.getInputStream()).read(buffer);
                JSONObject response = new JSONObject(String.valueOf(buffer));
                if(response.getString("result").equals("success")){
                    this.account = account;
                    this.password = password;
                    this.isLogin.postValue(true);
                    toast.setText("登录成功");
                    toast.show();
                    dialog.dismiss();
                }else{
                    toast.setText("登录失败");
                    toast.show();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                //System.exit(-1);
            }
        }).start();
    }

    public void register(String account, String password, Toast toast){
        new Thread(()->{
            try {
                URL url = new URL(GameURL.register);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                JSONObject object = new JSONObject();
                object.put("account", account);
                object.put("password", password);
                OutputStream stream = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
                writer.write(object.toString());
                writer.flush();
                writer.close();
                stream.close();
                connection.connect();
                char[] buffer = new char[100];
                new InputStreamReader(connection.getInputStream()).read(buffer);
                JSONObject response = new JSONObject(String.valueOf(buffer));
                if(response.getString("result").equals("success")){
                    toast.setText("注册成功");
                }else{
                    toast.setText("注册失败");
                }
                toast.show();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }).start();
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
