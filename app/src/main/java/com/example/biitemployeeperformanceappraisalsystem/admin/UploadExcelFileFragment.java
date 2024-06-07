package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.network.services.ChrService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EnrollmentService;

public class UploadExcelFileFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSIONS = 1000;
    private static final int PICK_EXCEL_REQUEST = 1001;
    private static final String TAG = "UploadExcelFileFragment";

    private ChrService chrService;
    private EnrollmentService enrollmentService;
    private View view;
    private TextView textFileName;
    private Button btnChooseFile, btnUploadFile;
    private Uri fileUri;
    private boolean isChr = true;

    public UploadExcelFileFragment() {
        // Required empty public constructor
    }

    public UploadExcelFileFragment(boolean isChr) {
        this.isChr = isChr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_excel_file, container, false);
        chrService = new ChrService(getContext());
        enrollmentService = new EnrollmentService(getContext());
        textFileName = view.findViewById(R.id.text_file_name);
        btnChooseFile = view.findViewById(R.id.btn_choose_file);
        btnUploadFile = view.findViewById(R.id.btn_upload_file);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChooseFile.setOnClickListener(v -> openFileChooser());
        btnUploadFile.setOnClickListener(v -> {
            if (fileUri != null) {
                if (hasPermissions()) {
                    uploadFile();
                } else {
                    requestPermissions();
                    uploadFile();
                }
            } else {
                Toast.makeText(getContext(), "Please choose a file first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), PICK_EXCEL_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();
                if (fileUri != null) {
                    textFileName.setText(fileUri.getPath());
                }
            }
        }
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getActivity().getPackageName())));
                startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_PERMISSIONS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (fileUri != null) {
                    uploadFile();
                } else {
                    Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Permissions denied by the user.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Permissions denied by the user.");
            }
        }
    }

    private void uploadFile() {
        if (fileUri != null) {
            if (isChr){
                chrService.uploadChr(fileUri);
            }else {
                enrollmentService.uploadEnrollmentFile(fileUri);
            }
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
