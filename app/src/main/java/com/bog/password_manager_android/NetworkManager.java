package com.bog.password_manager_android;


import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bog.password_manager_android.models.RegistrationModel;
import com.bog.password_manager_android.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkManager {
    public static final int NETWORK_ERROR = 0;
    public static final int NETWORK_SUCCES = 200;
    private static final NetworkManager MANAGER = new NetworkManager();
    private final Executor executor = Executors.newCachedThreadPool();
    private final String REGISTRATION_URL =  "https://backend-password-manager.herokuapp.com/api/user/";
    private final String LOAD_DATA_URL = "https://backend-password-manager.herokuapp.com/api/data/";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public interface RegistrationCallback {
        void onLoaded(Integer resultCode);
    }

    public interface DownloadCallback {
        void onDownLoaded(String cipherData, String iv, int resultCode);
    }

    public interface UploadCallback {
        void onUploaded(Integer resultCode);
    }


    public static NetworkManager getInstance() {
        return MANAGER;
    }

    private RegistrationCallback registrationCallback;
    private DownloadCallback downloadCallback;
    private UploadCallback uploadCallback;

    public void setRegistrationCallback(RegistrationCallback callback) {
        this.registrationCallback = callback;
    }

    public void setDownloadCallback(DownloadCallback callback) {
        this.downloadCallback = callback;
    }

    public void setUploadCallback(UploadCallback callback) {
        this.uploadCallback = callback;
    }


    public void registrate(final String username, final String password) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Integer resultCode;
                try {
                    RegistrationModel model = new RegistrationModel(username, password);
                    Gson gson = new Gson();
                    String body = gson.toJson(model);
                    resultCode = sendPostRequest(REGISTRATION_URL, body);
                } catch (IOException e) {
                    resultCode = NETWORK_ERROR;
                }
                notifyRegisrationResult(resultCode);
            }
        });
    }

    private class DoubleStringStructure {
        public String cipherData;
        public String password;
    }

    public void downloadData(final String username, final String password) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = LOAD_DATA_URL + "?username=" + username  + "&password=" + password;
                    Response response = sendGetRequest(url);
                    if (response.code() != 200) {
                        notifyDownloadResult(null, null, NETWORK_ERROR);
                    } else {
                        DoubleStringStructure result = new GsonBuilder().create().fromJson(response.body().string(), DoubleStringStructure.class);
                        notifyDownloadResult(result.cipherData, result.password, 200);
                    }
                } catch (IOException e) {
                    notifyDownloadResult(null, null, NETWORK_ERROR);
                }
            }
        });
    }


    public void uploadData(final String username, final String password, final String cipherData, final String iv)  {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    UserModel data = new UserModel(username, password, cipherData, iv);
                    Gson gson = new Gson();
                    String body = gson.toJson(data);
                    Integer result = sendPostRequest(LOAD_DATA_URL, body);
                    notifyUploadResult(result);
                } catch (IOException e) {
                    notifyUploadResult(NETWORK_ERROR);
                }
            }
        });
    }

    private void notifyUploadResult(final Integer result) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (uploadCallback != null) {
                    uploadCallback.onUploaded(result);
                }
            }
        });
    }

    private void notifyRegisrationResult(final Integer resultCode) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (registrationCallback != null) {
                    registrationCallback.onLoaded(resultCode);
                }
            }
        });
    }

    private void notifyDownloadResult(final String cipherData, final String iv, final Integer resultCode) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (downloadCallback != null) {
                    downloadCallback.onDownLoaded(cipherData, iv, resultCode);
                }
            }
        });
    }

    private Response sendGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public Integer sendPostRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        try {
            return response.code();
        } finally {
            response.close();
        }
    }
}
