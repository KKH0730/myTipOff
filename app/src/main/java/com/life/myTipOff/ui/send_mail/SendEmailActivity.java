package com.life.myTipOff.ui.send_mail;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.life.myTipOff.R;
import com.life.myTipOff.databinding.ActivitySendEmailBinding;
import com.life.myTipOff.model.AttachItem;
import com.life.myTipOff.model.AttachListener;
import com.life.myTipOff.utils.ConvertUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendEmailActivity extends AppCompatActivity implements View.OnClickListener {
    public static String PARAM_MEDIA = "medai";
    public static String PARAM_KBS = "kbs";
    public static String PARAM_MBC = "mbc";
    public static String PARAM_SBS = "sbs";
    public static String PARAM_JTBC = "jtbc";
    public static String PARAM_YTN = "ytn";
    public static String PARAM_YONHAP = "yonHap";

    private ActivitySendEmailBinding binding;

    private final String EMAIL_KBS = "kbs1234@kbs.co.kr";
    private final String EMAIL_MBC = "mbcjebo@mbc.co.kr";
    private final String EMAIL_SBS = "newsbs@sbs.co.kr";
    private final String EMAIL_JTBC = "jebo@jtbc.co.kr";
    private final String EMAIL_YTN = "social@ytn.co.kr";
    private final String EMAIL_YONHAP = "jebo@yna.co.kr";
    String email = "";

    @SuppressLint("NotifyDataSetChanged")
    private ActivityResultLauncher<Intent> fileSelectLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        AttachAdapter attachAdapter = (AttachAdapter) binding.rvAttach.getAdapter();

        ClipData clipData = result.getData().getClipData();
        List<Uri> uris = new ArrayList();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                uris.add(item.getUri());
            }
        } else {
            uris.add(result.getData().getData());
        }

        for (Uri selectedUri : uris) {
            File file = ConvertUtil.INSTANCE.convertUriToFile(this, Uri.parse(selectedUri.toString()));
            if (file != null) {
                Float fileSize = ConvertUtil.INSTANCE.getFileSize(file);

                Float totalSize = fileSize;
                for (AttachItem element : attachAdapter.getItems()) {
                    totalSize += element.getSize();
                }

                if (totalSize > 500f) {
                    Toast.makeText(getApplicationContext(), getString(R.string.report_attach_limit_guide), Toast.LENGTH_SHORT).show();
                } else {
                    attachAdapter.addItem(new AttachItem(file.getName(), selectedUri.toString(), fileSize));
                    attachAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    private ActivityResultLauncher<Intent> emailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_email);

        init();
        setAttachRecyclerView();
        setNestedScrollingEnabled();
        setSpinner();
        setTextWatcher();
    }


    private void init() {
        String param = getIntent().getStringExtra(SendEmailActivity.PARAM_MEDIA);

        if (param.equals(SendEmailActivity.PARAM_KBS)) {
            email = EMAIL_KBS;
            binding.ivMedia.setImageResource(R.drawable.img_kbs_rectengle);
            binding.tvMedia.setText(getString(R.string.report_media_kbs));
        } else if (param.equals(SendEmailActivity.PARAM_MBC)) {
            email = EMAIL_MBC;
            binding.ivMedia.setImageResource(R.drawable.img_mbc_rectengle);
            binding.tvMedia.setText(getString(R.string.report_media_mbc));
        } else if (param.equals(SendEmailActivity.PARAM_SBS)) {
            email = EMAIL_SBS;
            binding.ivMedia.setImageResource(R.drawable.img_sbs_rectengle);
            binding.tvMedia.setText(getString(R.string.report_media_sbs));
        } else if (param.equals(SendEmailActivity.PARAM_JTBC)) {
            email = EMAIL_JTBC;
            binding.ivMedia.setImageResource(R.drawable.img_jtbc_rectengle);
            binding.tvMedia.setText(getString(R.string.report_media_jtbc));
        } else if (param.equals(SendEmailActivity.PARAM_YTN)) {
            email = EMAIL_YTN;
            binding.ivMedia.setImageResource(R.drawable.img_ytn_rectengle);
            binding.tvMedia.setText(getString(R.string.report_media_ytn));
        } else {
            email = EMAIL_YONHAP;
            binding.ivMedia.setImageResource(R.drawable.img_yonhap);
            binding.tvMedia.setText(getString(R.string.report_media_yonhap));
        }

        binding.tvTerm.setVerticalScrollBarEnabled(true);
        binding.tvTerm.setScrollbarFadingEnabled(false);

        binding.flBackButton.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvReport.setOnClickListener(this);
        binding.flAgreeButton.setOnClickListener(this);
        binding.clAttach.setOnClickListener(this);
    }

    private void setAttachRecyclerView() {
        binding.rvAttach.setAdapter(new AttachAdapter());
        ((AttachAdapter) binding.rvAttach.getAdapter()).setOnAttachListener(new AttachListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void removeAttachFile(int id) {
                ((AttachAdapter) binding.rvAttach.getAdapter()).removeItem(id);
                ((AttachAdapter) binding.rvAttach.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setNestedScrollingEnabled() {
        binding.etContent.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.etContent) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        });
    }

    private void setSpinner() {
        ArrayList<String> items = new ArrayList<>();
        items.add(getString(R.string.spinner_item1));
        items.add(getString(R.string.spinner_item2));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {

                    }
                    case 1: {

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTextWatcher() {
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString();
                String content = Objects.requireNonNull(binding.etContent.getText()).toString();
                String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();
                controlReportButton(name, content, phone, binding.cbTerm.isChecked());
            }
        });

        binding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = Objects.requireNonNull(binding.etName.getText()).toString();
                String content = Objects.requireNonNull(binding.etContent.getText()).toString();
                String phone = s.toString();
                controlReportButton(name, content, phone, binding.cbTerm.isChecked());
            }
        });

        binding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = Objects.requireNonNull(binding.etName.getText()).toString();
                String content = Objects.requireNonNull(binding.etContent.getText()).toString();
                String phone = s.toString();
                controlReportButton(name, content, phone, binding.cbTerm.isChecked());
            }
        });

        binding.cbTerm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String name = Objects.requireNonNull(binding.etName.getText()).toString();
            String content = Objects.requireNonNull(binding.etContent.getText()).toString();
            String phone = Objects.requireNonNull(binding.etPhone.getText()).toString();

            controlReportButton(name, content, phone, isChecked);
        });
    }

    private void controlReportButton(
            String name,
            String content,
            String phone,
            Boolean isAgree
    ) {
        if (name.length() != 0 && content.length() != 0 && phone.length() != 0 && isAgree) {
            binding.tvReport.setEnabled(true);
        } else {
            binding.tvReport.setEnabled(false);
        }
    }

    public void openDirectory() {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fileSelectLauncher.launch(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.flBackButton || v.getId() == R.id.tvCancel) {
            finish();
        } else if (v.getId() == R.id.flAgreeButton) {
            binding.cbTerm.setChecked(!binding.cbTerm.isChecked());
        } else if (v.getId() == R.id.tvReport) {
            String sb = "이름 : " + Objects.requireNonNull(binding.etName.getText()) + "(" + binding.spinner.getSelectedItem().toString() + ")" +
                    "\n" +
                    "전화번호 : " + Objects.requireNonNull(binding.etPhone.getText()) +
                    "\n" +
                    "제보내용 : " + Objects.requireNonNull(binding.etContent.getText());

            List<AttachItem> list = ((AttachAdapter) binding.rvAttach.getAdapter()).getItems();
            ArrayList<Uri> uris = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                uris.add(Uri.parse(list.get(i).getUri()));
            }

            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, sb);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            if (intent.resolveActivity(getPackageManager()) != null) {
                emailLauncher.launch(intent);
            }
        } else if (v.getId() == R.id.clAttach) {
            openDirectory();
        }
    }
}

