package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.helper.RealPathUtil;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.ChrServiceListener;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChrService {
    private Context context;
    private ChrServiceListener chrServiceListener;
    public ChrService(Context context){
        chrServiceListener = RetrofitClient.getRetrofitInstance().create(ChrServiceListener.class);
        this.context = context;
    }

    public void uploadChr(Uri fileUri) {
        if (fileUri != null) {
            String filePath = RealPathUtil.getRealPath(context, fileUri);
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {

                    // Create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("application/vnd.ms-excel"), file);

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body = MultipartBody.Part.createFormData("chr", file.getName(), requestFile);
                    Call<ResponseBody> call = chrServiceListener.uploadCHR(body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context.getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context.getApplicationContext(), "Failed to upload file: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("upload error", "error uploading file", t);
                            Toast.makeText(context.getApplicationContext(), "Error uploading file: " + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context.getApplicationContext(), "File does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context.getApplicationContext(), "Failed to get file path", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context.getApplicationContext(), "File URI is null", Toast.LENGTH_SHORT).show();
        }
    }

}
