package edu.uw.tcss450.group6project.io;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/** Taken from Lab 3. Handles the list of tasks to do asynchronously.
 *
 * @author Chase Alder
 * @author Charles Bryan
 * @version 1.0
 */
public class RequestQueueSingleton {
    private static edu.uw.tcss450.group6project.io.RequestQueueSingleton instance;
    private static Context context;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private RequestQueueSingleton(Context context) {
        edu.uw.tcss450.group6project.io.RequestQueueSingleton.context = context;
        mRequestQueue = getmRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /** Getter, retrieves the current instance. Asynchronous.
     *
     * @param context The current context
     * @return an instance of the class
     */
    public static synchronized edu.uw.tcss450.group6project.io.RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new edu.uw.tcss450.group6project.io.RequestQueueSingleton(context);
        }
        return instance;
    }

    /** Getter for the queue.
     *
     * @return the current state of the queue
     */
    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    /** Adds a request to the queue.
     *
     * @param req The request to be added
     * @param <T> The type of request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getmRequestQueue().add(req);
    }

    /** Gets the ImageLoader
     *
     * @return the ImageLoader
     */
    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
