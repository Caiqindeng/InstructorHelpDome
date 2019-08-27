package com.example.instructorhelpdome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.MyUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class SelectphotoActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_take_photo;
    private TextView text_pick_photo;
    private TextView text_cancle;
    private File mFile;
    private Bitmap mBitmap;
    String path = "";
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CUT_PHOTO = 3;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectphoto);
        initview();
    }

    private void initview() {
        text_cancle=(TextView)findViewById(R.id.text_cancle);
        text_take_photo=(TextView)findViewById(R.id.text_take_photo);
        text_pick_photo=(TextView)findViewById(R.id.text_pick_photo);
        text_cancle.setOnClickListener(this);
        text_take_photo.setOnClickListener(this);
        text_pick_photo.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_cancle:
                //取消
                finish();
                break;
            case R.id.text_take_photo:
                pickImageFromCamera();

                break;
            case R.id.text_pick_photo:
                pickImageFromAlbum();

                break;
        }
    }
    //从相册选择
    private void pickImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    //拍照
    private void pickImageFromCamera() {
        File outputImage=new File(Environment.getExternalStorageDirectory(),
                "output_image.jpg");//创建File对象，用于存储拍照后的图片，获取sd卡根目录
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        imageUri=Uri.fromFile(outputImage);//File对象转化为Uri对象
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO); //启动相机程序

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    cropPhoto(imageUri);
                    break;
                case CHOOSE_PHOTO:
                    if (data == null || data.getData() == null) {
                        return;
                    }
                    try {
                        Uri uri = data.getData();
                        cropPhoto(uri);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                case CUT_PHOTO:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        //在这里获得了剪裁后的Bitmap对象，可以用于上传
                        Bitmap image = bundle.getParcelable("data");
                        Log.e("我是裁剪后",""+image);
                        //设置到ImageView上
                       // img.setImageBitmap(image);
                        //也可以进行一些保存、压缩等操作后上传
                        path = saveImage("crop", image);
                        Log.e("我是裁剪后2",""+path);

                        setPicToView(data);
                    }
                    break;


            }
        }

    }

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CUT_PHOTO);
    }



    private void setPicToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            mBitmap = bundle.getParcelable("data");
            if(mFile != null){
                path = mFile.getPath();
            }

            Toast.makeText(SelectphotoActivity.this,"path:"+path,Toast.LENGTH_SHORT).show();
            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(SelectphotoActivity.this, "pic is success", Toast.LENGTH_SHORT).show();
                        MyUser myUser = MyUser.getCurrentUser(MyUser.class);
                        //得到上传的图片地址
                        String fileUrl = bmobFile.getFileUrl();
                        myUser.setImages(bmobFile);
                        //更新图片地址
                        myUser.update(myUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(SelectphotoActivity.this, "update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

        }
    }
}






