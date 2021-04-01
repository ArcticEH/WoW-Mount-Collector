
package ca.mohawk.kolar.api;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ca.mohawk.kolar.activity.DetailActivity;

/**
 * AsyncTask used to download image from URL
 */
public class DownloadImageTask
        extends AsyncTask<String, Void, Bitmap> {
    public static String TAG = "==DownloadImageTask==";

    // Statically set ok result code
    public static int HTTP_OK = 200;

    /**
     * Does background request for image with URL
     */
    protected Bitmap doInBackground(String... urls) {
        Log.d(TAG,"doInBackground()");

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

    /**
     * Set image once result is retrieved
     * @param result The bitmap result
     */
    protected void onPostExecute(Bitmap result) {
        Log.d(TAG,"onPostExecute()");

        // Only set image if result has been set (successful request)
        if (result != null) {
            DetailActivity.instance.SetMountImage(result);
        }
    }

}
