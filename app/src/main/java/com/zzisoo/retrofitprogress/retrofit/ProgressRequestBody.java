package com.zzisoo.retrofitprogress.retrofit;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {
    private static final String TAG = "ProgressRequestBody";
    public static final float FLOAT_PERCENT_DIV = 0.01f;

    public File getmFile() {
        return mFile;
    }

    public MediaType getmContentType() {
        return mContentType;
    }

    public UploadCallbacks getmListener() {
        return mListener;
    }
    private File mFile;
    private MediaType mContentType;
    private UploadCallbacks mListener;

    private static final int DEFAULT_BUFFER_SIZE = 4096;


    public interface UploadCallbacks {
        void onProgressUpdate(String path, int percent);

        void onError(int position);

        void onFinish(int position, String urlId);
    }

    public ProgressRequestBody(MediaType contentType,final File file, final UploadCallbacks listener) {
        mContentType = contentType;
        mFile = file;
        mListener = listener;
    }

    public static ProgressRequestBody createImage( MediaType contentType, final File file,UploadCallbacks cb) {
        if (file == null) throw new NullPointerException("content == null");
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(contentType,file, cb) ;
        return progressRequestBody;
    }


    private class ProgressUpdater implements Runnable {
        private final String mFilePath;
        private int mPercent;
        private double mUploaded;
        private double mTotal;

        public ProgressUpdater(String filePath, long uploaded, long total, int percent) {
            mUploaded = uploaded;
            mTotal = total;
            mPercent = percent;
            mFilePath = filePath ;
        }

        @Override
        public void run() {
            // Log.e(TAG, mFilePath + ">>" + mPercent);
            if (mListener != null) {
                mListener.onProgressUpdate(mFilePath, mPercent);
            }
        }
    }
    @Override
    public MediaType contentType() {
        return getmContentType();
    }

    @Override
    public long contentLength() {
        return getmFile().length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = contentLength();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(getmFile());
        long uploaded = 0;
        int lastPercent= 0;
        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                uploaded += read;
                int percent = (int) (uploaded / (fileLength* FLOAT_PERCENT_DIV));
                if(lastPercent<percent) {
                    lastPercent =percent;
                    handler.post(new ProgressUpdater(getmFile().getAbsolutePath(), uploaded, fileLength, percent));
                }
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}