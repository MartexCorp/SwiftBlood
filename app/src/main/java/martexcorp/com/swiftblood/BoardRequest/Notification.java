package martexcorp.com.swiftblood.BoardRequest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notification extends AsyncTask<Void,Void,Void> {
    private static String ServerKey = "key=AAAAFwPN2WI:APA91bFUC07kc2sdtIc37NvGHa7K4RwZ1EC4SFG7-6g3jL8zswjxm3BexX9pEsz1e9d6-JD4rtALX4XOrdvE3fJ7pUthKwh8ZSWtjrTgxQ3zpQ6SNCi6LCfKzEnfckWjIAw-WkYR7hOe";
    private String topic, title, body;


    public Notification(String topic, String title, String body){

        this.topic=topic;
        this.title=title;
        this.body=body;

        execute();

    }

    @Override
    protected Void doInBackground(Void... voids) {



        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", ServerKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            JSONObject json = new JSONObject();

            json.put("to", topic);


            JSONObject info = new JSONObject();
            info.put("title", title);   // Notification title
            info.put("body", body); // Notification body

            json.put("data", info);
            json.put("priority","high");
            json.put("content_available",true);


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

            Log.d("END CONNECTION:","FCM SUCCESSFUL");

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }


        return null;
    }
}


