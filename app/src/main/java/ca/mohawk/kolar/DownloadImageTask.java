
package ca.mohawk.kolar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DownloadImageTask
        extends AsyncTask<String, Void, Bitmap> {
    public static String TAG = "==DownloadImageTask==";
    public static int HTTP_OK = 200;

    protected Bitmap doInBackground(String... urls) {
        // Use the URL Connection interface to download the URL
        Bitmap bmp = null;
        Log.d(TAG, "do background " + urls[0]);
        // URL connection must be done in a try/catch
        int statusCode = -1;
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            statusCode = conn.getResponseCode();
            if (statusCode == HTTP_OK) {
                bmp = BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "bad URL " + e);
        } catch (IOException e) {
            Log.d(TAG, "bad I/O " + e);
        }
        Log.d(TAG, "done " + statusCode);
        return bmp;
    }


    protected void onPostExecute(Bitmap result) {
        Log.d(TAG,"onPostExecute()");
        if (result != null) {
            DetailActivity.instance.SetMountImage(result);
        }
    }

}
