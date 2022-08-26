package com.example.smartindiahackathon.ui.home;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smartindiahackathon.Convertor_Layout;
import com.example.smartindiahackathon.OPEN_DOC_FILE;
import com.example.smartindiahackathon.OPEN_PDF_FILE;
import com.example.smartindiahackathon.R;
import com.example.smartindiahackathon.databinding.FragmentHomeBinding;
import com.example.smartindiahackathon.ui.doc.DocPreviewActivity;
import com.example.smartindiahackathon.ui.epubReader.EpubReaderMainActivity;
import com.example.smartindiahackathon.ui.epubReader.FileChooser;
import com.example.smartindiahackathon.ui.pdfReader.pdfdigitalsignature.DigitalSignatureActivity;
import com.example.smartindiahackathon.ui.pdfReader.pdfdigitalsignature.PdfMainActivity;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* getting user permission for external storage */
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        setClicks();
    }

    private void setClicks() {
        View view1 = requireView().findViewById(R.id.rectangle_1);
        view1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), Convertor_Layout.class);
            startActivity(intent);
        });
        View view2 = requireView().findViewById(R.id.rectangle_2);
        view2.setOnClickListener(view -> {
            Intent I = new Intent(requireContext(), PdfMainActivity.class);
            startActivity(I);
        });
        View view3 = requireView().findViewById(R.id.rectangle_4);
        view3.setOnClickListener(view -> {
            OpenDocManager();
        });
        View view4 = requireView().findViewById(R.id.rectangle_5);
        view4.setOnClickListener(view -> {
            OpenEpubManager();
        });


    }


    public void OpenDocManager() {
//        ExFilePicker exFilePicker = new ExFilePicker();
//        exFilePicker.setCanChooseOnlyOneItem(true);
//        exFilePicker.setShowOnlyExtensions("docx");
//        exFilePicker.setQuitButtonEnabled(true);
//        exFilePicker.setHideHiddenFilesEnabled(true);
//        exFilePicker.setNewFolderButtonDisabled(true);
//        exFilePicker.start(requireActivity(), 1);
        Intent I = new Intent(requireContext(), DocPreviewActivity.class);
        startActivity(I);



    }

    public void OpenEpubManager() {

        Intent I = new Intent(requireContext(), FileChooser.class);
        startActivity(I);

//        ExFilePicker exFilePicker = new ExFilePicker();
//        exFilePicker.setCanChooseOnlyOneItem(true);
//        exFilePicker.setShowOnlyExtensions("epub");
//        exFilePicker.setQuitButtonEnabled(true);
//        exFilePicker.setHideHiddenFilesEnabled(true);
//        exFilePicker.setNewFolderButtonDisabled(true);
//        exFilePicker.start(requireActivity(), 3);
    }


}