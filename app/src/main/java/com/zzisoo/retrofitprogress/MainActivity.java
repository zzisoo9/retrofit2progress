package com.zzisoo.retrofitprogress;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.zzisoo.retrofitprogress.retrofit.FileUploadService;
import com.zzisoo.retrofitprogress.retrofit.ProgressRequestBody;
import com.zzisoo.retrofitprogress.retrofit.ServiceGenerator;

import java.io.File;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileupload();
            }
        });




    }

    private void fileupload() {
        final TextView tvTextview = (TextView) findViewById(R.id.tv_textview);
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        String description = "hello, this is description speaking";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"image.jpg"); // /sdcard/image.jpg

        ProgressRequestBody requestBody = ProgressRequestBody.createImage(MediaType.parse("multipart/form-data"), file, new ProgressRequestBody.UploadCallbacks() {

            @Override
            public void onProgressUpdate(String path, int percent) {
                tvTextview.setText(path + "\t>>>"+percent);
            }

            @Override
            public void onError(int position) {

            }

            @Override
            public void onFinish(int position, String urlId) {

            }
        });

        Call<String> call = service.upload(requestBody, description);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Upload","onFailure" + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
