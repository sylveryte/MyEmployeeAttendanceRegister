package com.codedleaf.sylveryte.myemployeeattendanceregister.Editing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.codedleaf.sylveryte.myemployeeattendanceregister.CircleTransform;
import com.codedleaf.sylveryte.myemployeeattendanceregister.CodedleafTools;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Labs.EmployeeLab;
import com.codedleaf.sylveryte.myemployeeattendanceregister.Models.Employee;
import com.codedleaf.sylveryte.myemployeeattendanceregister.R;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

/**
 * Created by sylveryte on 20/7/16.
 *
 * Copyright (C) 2016 sylveryte@codedleaf <codedleaf@gmail.com>
 *
 * This file is part of My Employee Attendance Register.
 *
 */
public class EmployeeAdditionFragmentDialog extends DialogFragment implements ImageChooserListener{

    private static final String ARGS_CODE="empargscode";
    private static final int TAKE_NEW_PICTURE=0;
    private static final int CHOOSE_FROM_GALLERY=1;

    private static final int IMAGE_WIDTH=600;
    private static final int IMAGE_HEIGHT=600;

    private EditText mName;
    private EditText mAddress;
    private EditText mNote;
    private EditText mAge;

    private RadioButton mRadioButtonMale;
    private RadioButton mRadioButtonFemale;

    private ImageButton mChooseImageButton;

    private ImageView mImagePhoto;

    private Employee mEmployee;

    private File mPhotoFile;

    private String mNewFilePath;

    private ImageChooserManager mImageChooserManager;
    private String mediaPath;

//    private String mNewPhotoPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view= LayoutInflater.from(getActivity()).inflate(R.layout.employee_addition_fragment,null,false);

        mName=(EditText)view.findViewById(R.id.employee_add_name);
        mAddress=(EditText)view.findViewById(R.id.employee_add_address);
        mNote=(EditText)view.findViewById(R.id.employee_add_note);
        mAge=(EditText)view.findViewById(R.id.employee_add_age);
        RadioGroup radioGroupMaleFemale = (RadioGroup) view.findViewById(R.id.employee_add_radiobuttongroup_malefemale);
        mRadioButtonFemale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_female);
        mRadioButtonMale=(RadioButton)view.findViewById(R.id.employee_add_radiobutton_male);
        ImageButton done = (ImageButton) view.findViewById(R.id.emp_addition_done);
        mChooseImageButton=(ImageButton)view.findViewById(R.id.emp_addition_choose_photo);
        mImagePhoto=(ImageView)view.findViewById(R.id.employee_addition_photo);

        mChooseImageButton.setOnClickListener(new ChooseButtonListner());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation errors

                if (mName.getText().toString().trim().isEmpty())
                {
                    mName.setError("Please Enter Name");
                    return;
                }
                if (mAge.getText().toString().trim().isEmpty())
                {
                    mAge.setError("Please enter age");
                    return;
                }

                if(!(mRadioButtonMale.isChecked()||mRadioButtonFemale.isChecked()))
                {
                    mRadioButtonMale.setError("Please Select Gender");
                    return;
                }

                saveEmployee(mEmployee);
                getDialog().dismiss();
            }
        });

        radioGroupMaleFemale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.employee_add_radiobutton_male)
                {
                    mRadioButtonFemale.setChecked(false);
                    mRadioButtonMale.setChecked(true);
                }else if (checkedId==R.id.employee_add_radiobutton_female)
                {
                    mRadioButtonMale.setChecked(false);
                    mRadioButtonFemale.setChecked(true);
                }
            }
        });




        Bundle args=getArguments();
        //differentiate between edit and add calls
        if (args!=null)
        {
            UUID uuid=(UUID)args.getSerializable(ARGS_CODE);

            mEmployee= EmployeeLab.getInstanceOf(getActivity()).getEmployeeById(uuid);
            updateData();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    class ChooseButtonListner implements View.OnClickListener
    {
        CharSequence options[]=new CharSequence[]{"Take new Photo","Select from Gallery","Remove Photo"};
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("Edit Photo");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Fragment callingFragment=getParentFragment();

//                    Log.i(ARGS_CODE,"pic taking bro "+which+" activity="+getActivity());

                    if (mEmployee==null)
                    {
                        mEmployee=new Employee();
                    }
                    if (mPhotoFile==null)
                        mPhotoFile=EmployeeLab.getInstanceOf(getContext()).getEmpPhotoFile(mEmployee);
                    switch (which)
                    {
                        case 0:
                        {
                            takePicture();
                            break;
                        }
                        case 1:
                        {
                            chooseImage();
                            break;
                        }
                        case 2:
                        {
                            if (mPhotoFile.exists())
                            {
                                if (mPhotoFile.delete())
                                {
                                    mNewFilePath=null;
                                    setPlaceHolderPhoto();
                                    Toast.makeText(getContext(),"Photo Removed",Toast.LENGTH_LONG)
                                        .show();
                                    invalidatePhoto();
                                }
                            }
                        }
                    }

                }
            });
            builder.show();
        }
    }

   private void chooseImage()
   {
       mImageChooserManager=new ImageChooserManager(this,ChooserType.REQUEST_PICK_PICTURE,false);
       mImageChooserManager.setImageChooserListener(this);
       try {
           mediaPath=mImageChooserManager.choose();
       } catch (ChooserException e) {
           e.printStackTrace();
       }
   }

    private void takePicture()
    {
        mImageChooserManager=new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE);
        mImageChooserManager.setImageChooserListener(this);
        try {
            mediaPath=mImageChooserManager.choose();
        } catch (ChooserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (mImageChooserManager == null) {
                mImageChooserManager = new ImageChooserManager(this, requestCode, true);
                mImageChooserManager.setImageChooserListener(this);
                mImageChooserManager.reinitialize(mediaPath);
            }
            mImageChooserManager.submit(requestCode, data);
        }
    }

    private void setPlaceHolderPhoto() {
       EmployeeLab.getInstanceOf(getContext()).setEmpPlaceHolder(mEmployee,mImagePhoto);
    }

    private void updateData()
    {
        mName.setText(mEmployee.getName());
        mAddress.setText(mEmployee.getAddress());
        mNote.setText(mEmployee.getNote());

        if(mEmployee.isMale())
        {
            mRadioButtonMale.setChecked(true);
            mRadioButtonFemale.setChecked(false);
        }else if (!mEmployee.isMale())
        {
            mRadioButtonFemale.setChecked(true);
            mRadioButtonMale.setChecked(false);
        }
        updatePhoto();
        mAge.setText(mEmployee.getAgeString());
    }

    private void updatePhoto()
    {
        if (mEmployee==null)
        {
            setPlaceHolderPhoto();
            return;
        }
        if (mPhotoFile==null)
        {
            mPhotoFile=EmployeeLab.getInstanceOf(getContext()).getEmpPhotoFile(mEmployee);
            if (!mPhotoFile.exists())
            {
                setPlaceHolderPhoto();
                return;
            }
        }
        setPhoto(mPhotoFile.getPath());
    }

    private void  saveEmployee(Employee employee)
    {
        if (employee==null)
        {
            mEmployee=new Employee();
        }
        if (mPhotoFile==null)
            mPhotoFile=EmployeeLab.getInstanceOf(getContext()).getEmpPhotoFile(mEmployee);

        if (mNewFilePath!=null)
            new savePhoto().execute(new AsyncTaskParas(mNewFilePath,mPhotoFile));
        else
            invalidatePhoto();

        EmployeeLab.getInstanceOf(getActivity()).addEmployee(mEmployee);
        mEmployee.setActive(true);
        mEmployee.setName(mName.getText().toString().trim());
        mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
        mEmployee.setAddress(mAddress.getText().toString().trim());
        mEmployee.setNote(mNote.getText().toString().trim());
        mEmployee.setMale(mRadioButtonMale.isChecked());
        mEmployee.updateMyDB(getActivity());
    }

    private void invalidatePhoto() {
        EmployeeLab.getInstanceOf(getContext()).invalidatePhotoCache(mPhotoFile.getPath());
    }

    class AsyncTaskParas
    {
        String mFilePath;
        File mFile;

        public AsyncTaskParas(String filePath,File file) {
            mFilePath=filePath;
            mFile=file;
        }
    }

    class savePhoto extends AsyncTask<AsyncTaskParas,Void,Void>
    {
        @Override
        protected Void doInBackground(AsyncTaskParas... params) {
            CodedleafTools.saveFile(params[0].mFilePath,params[0].mFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            invalidatePhoto();
        }
    }

    @Override
    public void onImageChosen(ChosenImage chosenImage) {
        mNewFilePath=chosenImage.getFilePathOriginal();
        setPhoto(mNewFilePath);
    }

    @Override
    public void onError(String s) {
    }

    @Override
    public void onImagesChosen(ChosenImages chosenImages) {
        mNewFilePath=chosenImages.getImage(0).getFilePathOriginal();
        setPhoto(mNewFilePath);
        Toast.makeText(getContext(),"How did this happen?",Toast.LENGTH_SHORT)
                .show();
    }

    private void setPhoto(final String newFilePath)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Picasso.with(getContext())
//                        .load(newFilePath)
//                        .resize(450,450)
//                        .transform(new CircleTransform())
//                        .centerCrop()
//                        .centerInside()
//                        .into(mImagePhoto);
                mImagePhoto.setImageBitmap(CodedleafTools.getScaledBitmap(newFilePath,250,250));
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEmployee!=null)
            if (!EmployeeLab.getInstanceOf(getContext()).doesExist(mEmployee))
            {
                if (mPhotoFile.exists())
                    mPhotoFile.delete();
            }
    }


    public static EmployeeAdditionFragmentDialog getDialogFrag(@Nullable UUID uuid)
    {
        if (uuid==null)
            return new EmployeeAdditionFragmentDialog();
        else
        {
            EmployeeAdditionFragmentDialog fragment=new EmployeeAdditionFragmentDialog();
            Bundle args=new Bundle();
            args.putSerializable(ARGS_CODE,uuid);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
