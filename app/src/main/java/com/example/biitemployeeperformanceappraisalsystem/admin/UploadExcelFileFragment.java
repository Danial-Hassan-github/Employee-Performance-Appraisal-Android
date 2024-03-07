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

public class UploadExcelFileFragment extends Fragment {

    View view;
    TextView textFileName;
    Button btnChooseFile;
    private static final int PICK_EXCEL_REQUEST = 1000;

    public UploadExcelFileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_upload_excel_file, container, false);
        textFileName=view.findViewById(R.id.text_file_name);
        btnChooseFile = view.findViewById(R.id.btn_choose_file);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnChooseFile.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.ms-excel"); // Filter to only show Excel files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), PICK_EXCEL_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // Handle the selected Excel file URI here
                    textFileName.setText(uri.getPath().toString());
                    // Toast.makeText(getContext(), "Selected Excel File: " + uri.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
