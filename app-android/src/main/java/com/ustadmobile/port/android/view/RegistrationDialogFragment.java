package com.ustadmobile.port.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.toughra.ustadmobile.R;
import com.ustadmobile.core.controller.RegistrationPresenter;
import com.ustadmobile.core.impl.UstadMobileSystemImpl;
import com.ustadmobile.core.view.DismissableDialog;
import com.ustadmobile.core.view.RegistrationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by varuna on 7/28/2017.
 */

public class RegistrationDialogFragment extends UstadDialogFragment implements RegistrationView, DismissableDialog, View.OnClickListener {

    private View mView;

    private RegistrationPresenter mPresenter;

    private ArrayList<TextInputEditText> fieldList = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mView = inflater.inflate(R.layout.fragment_register_dialog, container, false);

        mView.findViewById(R.id.fragment_register_dialog_register_button).setOnClickListener(this);

        fieldList.add((TextInputEditText)mView.findViewById(R.id.fragment_register_dialog_username_text));
        fieldList.add((TextInputEditText)mView.findViewById(R.id.fragment_register_dialog_password_text));

        mPresenter = new RegistrationPresenter(getContext(), this);
        if(mResultListener != null)
            mPresenter.setResultListener(mResultListener);

        return mView;
    }

    @Override
    public void addField(int fieldName, int fieldType) {
        //TODO
        LinearLayout fieldLayout = (LinearLayout)mView.findViewById(
                R.id.fragment_register_dialog_field_layout);
        UstadMobileSystemImpl impl = UstadMobileSystemImpl.getInstance();
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextInputEditText editText = new TextInputEditText(getContext());
        editText.setId(fieldName);
        editText.setHint(UstadMobileSystemImpl.getInstance().getString(fieldName, getContext()));
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine();
        editText.setMinLines(1);
        editText.setMaxLines(1);

        textInputLayout.addView(editText);
        fieldLayout.addView(textInputLayout, params);
        TextInputEditText prevEl = fieldList.get(fieldList.size()-1);
        prevEl.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        fieldList.add(editText);
    }

    @Override
    public void onClick(View v) {
        //fragment_register_dialog_username_text
        //fragment_register_dialog_password_text
        switch(v.getId()){
            case R.id.fragment_register_dialog_register_button:
                String username = ((EditText)mView.findViewById(R.id.fragment_register_dialog_username_text)).getText().toString();
                String password = ((EditText)mView.findViewById(R.id.fragment_register_dialog_password_text)).getText().toString();
                Hashtable fieldMap = new Hashtable();
                int[] allFields = mPresenter.extraFields;
                for(int field:allFields){
                    String value = ((EditText)mView.findViewById(field)).getText().toString();
                    fieldMap.put(field, value);
                }

                mPresenter.handleClickRegister(username, password, fieldMap);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(mPresenter != null)
            mPresenter.setResultListener(mResultListener);
    }
}