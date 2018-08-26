package wtf.atnpgo;/*
 * _____/\\\\\\\\\_____/\\\\\\\\\\\\\\\__/\\\\\_____/\\\__/\\\\\\\\\\\\\_______/\\\\\\\\\\\\_______/\\\\\______
 *  ___/\\\\\\\\\\\\\__\///////\\\/////__\/\\\\\\___\/\\\_\/\\\/////////\\\___/\\\//////////______/\\\///\\\____
 *   __/\\\/////////\\\_______\/\\\_______\/\\\/\\\__\/\\\_\/\\\_______\/\\\__/\\\_______________/\\\/__\///\\\__
 *    _\/\\\_______\/\\\_______\/\\\_______\/\\\//\\\_\/\\\_\/\\\\\\\\\\\\\/__\/\\\____/\\\\\\\__/\\\______\//\\\_
 *     _\/\\\\\\\\\\\\\\\_______\/\\\_______\/\\\\//\\\\/\\\_\/\\\/////////____\/\\\___\/////\\\_\/\\\_______\/\\\_
 *      _\/\\\/////////\\\_______\/\\\_______\/\\\_\//\\\/\\\_\/\\\_____________\/\\\_______\/\\\_\//\\\______/\\\__
 *       _\/\\\_______\/\\\_______\/\\\_______\/\\\__\//\\\\\\_\/\\\_____________\/\\\_______\/\\\__\///\\\__/\\\____
 *        _\/\\\_______\/\\\_______\/\\\_______\/\\\___\//\\\\\_\/\\\_____________\//\\\\\\\\\\\\/_____\///\\\\\/_____
 *         _\///________\///________\///________\///_____\/////__\///_______________\////////////_________\/////_______
 */

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

class Pusher {

    private final ExecutorService tPool = Executors.newSingleThreadExecutor();

    private final String webhookURL;
    private final String message;
    private final long delayMS;
    private final String name;
    private final String avatar;
    private Runnable doWork = new Runnable() {
        public void run() {
            JSONObject result = new JSONObject();
            if (name != null && name.length() > 0) {
                result.put("username", name);
            }
            if (avatar != null && avatar.length() > 0) {
                result.put("avatar_url", avatar);
            }
            result.put("content", message);
            String payload = result.toString();

            try {
                HttpURLConnection connection = (HttpURLConnection) (new URL(webhookURL)).openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", String.valueOf(payload.length()));
                connection.setDoOutput(true);
                connection.setDoInput(true);

                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
                int code = connection.getResponseCode();
                if (code >= 200 && code < 300) {
                    System.out.println("success");
                } else {
                    System.out.println("fail");
                }
            } catch (IOException ex) {
                Logger.getLogger(Pusher.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pusher.class.getName()).log(Level.SEVERE, null, ex);
            }
            tPool.submit(doWork);
        }
    };

    Pusher(String webhookURL, String message, long delayMS, String name, String avatar) {
        this.webhookURL = webhookURL;
        this.message = message;
        this.delayMS = delayMS;
        this.name = name;
        this.avatar = avatar;
    }

    void start() {
        tPool.submit(doWork);
    }


}
