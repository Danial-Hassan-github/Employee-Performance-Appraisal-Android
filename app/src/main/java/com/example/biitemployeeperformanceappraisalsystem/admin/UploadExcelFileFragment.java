package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.network.services.ChrService;

public class UploadExcelFileFragment extends Fragment {
    ChrService chrService;
    View view;
    TextView textFileName;
    Button btnChooseFile, btnUploadFile;
    private static final int PICK_EXCEL_REQUEST = 1000;
    private Uri fileUri;
    private String filePath;

    public UploadExcelFileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_excel_file, container, false);
        chrService = new ChrService(getContext());
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
                chrService.uploadChr(filePath);
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
                    filePath = fileUri.getPath();
                    textFileName.setText(fileUri.getPath());
                }
            }
        }
    }
}
