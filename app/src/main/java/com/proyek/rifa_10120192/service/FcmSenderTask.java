package com.proyek.rifa_10120192.service;
/**
 * NIM : 10120160
 * Nama : Rendi Julianto
 * Kelas : IF-4
 */
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FcmSenderTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "FcmSenderTask";

    private String token;
    private  String serverKey = "AAAAv7__Syw:APA91bFRxKQJrNMbOJHrNha4MzEMfKvLOeDshv49Bqyuo8SZ-fIawEuxGFyWfYik88NsNPL-Z3VDDqa9upZeyAzbqOyx68dpoGcmGS7SJnUkYCGXW0lAuNpmq-qjfWQCfCmY4oll8yBT";

    private String title;

    private String body;

    public FcmSenderTask(String token,  String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String url = "https://fcm.googleapis.com/fcm/send";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set request method
            con.setRequestMethod("POST");

            // Set request headers
            con.setRequestProperty("Authorization", "key=" + serverKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Enable input and output streams
            con.setDoOutput(true);

            // JSON payload
            String payload = "{"
                    + "\"to\": \"" + token + "\","
                    + "\"data\": {"
                    + "\"body\": \""+body+"\","
                    + "\"title\": \""+title+"\","
                    + "}"
                    + "}";

            // Send POST request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(payload);
            wr.flush();
            wr.close();

            // Get response code
            int responseCode = con.getResponseCode();


            // Read response
            Scanner scanner = new Scanner(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            Log.d(TAG, "Notif Masuk");
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Notif Gagal");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            Log.d(TAG, "Response: " + response);
        } else {
            Log.e(TAG, "Error sending FCM message.");
        }
    }
}
