package com.example.technicalseminar.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.technicalseminar.R;
import com.example.technicalseminar.databinding.FragmentPowBinding;
import com.example.technicalseminar.manager.SharedPreferencesManager;

public class PowFragment extends DialogFragment implements View.OnClickListener {
    private FragmentPowBinding binding;
    private Context mContext;
    private SharedPreferencesManager prefs;

    public PowFragment() {
    }
    public static PowFragment newInstance(){
        return new PowFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPowBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = new SharedPreferencesManager(mContext);
        binding.edtSetPow.setText(String.valueOf(prefs.getPowValue()));
        binding.btnClose.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(dialog.getWindow() != null ){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_continue:
                if(binding.edtSetPow.getText()!=null){
                    String pow = binding.edtSetPow.getText().toString();
                    prefs.setPowValue(Integer.parseInt(pow));
                    if(getActivity()!=null){
                        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        dismiss();
                    }
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding=null;
        mContext=null;
    }
}