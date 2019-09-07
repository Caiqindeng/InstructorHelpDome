package com.example.instructorhelpdome;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instructorhelpdome.JavaBean.MyUser;
import com.example.instructorhelpdome.JavaBean.Release;
import com.fingerth.supdialogutils.SYSDiaLogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ReleaseActivity extends AppCompatActivity  implements View.OnClickListener{
    private ImageButton back_btn;
    private EditText f_title;
    private EditText f_price;
    private EditText f_phone;
    private EditText f_describe;
    private byte[] image;
    private Button fbtn;
    private ImageButton imageButton;
    public Release mRelease=new Release();
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        initView();
    }

    private void initView() {
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        f_title=(EditText)findViewById(R.id.f_title);
        f_price=(EditText)findViewById(R.id.f_price);

        f_phone=(EditText)findViewById(R.id.f_phone);
        f_describe=(EditText)findViewById(R.id.f_describe);

        fbtn=(Button)findViewById(R.id.f_btn);
        fbtn.setOnClickListener(this);

        imageButton=(ImageButton)findViewById(R.id.m1_image);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.f_btn:
                ReleaseBtn();
                break;
            case R.id.m1_image:
                toImages();
                break;

        }
    }

    private void toImages() {
        if (ContextCompat.checkSelfPermission(ReleaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReleaseActivity.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //打开系统相册
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
             imagePath = c.getString(columnIndex);
             Log.i("msg",imagePath);
            showImage(imagePath);
            c.close();

        }
    }


    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        imageButton.setImageBitmap(bm);
    }


    private void ReleaseBtn() {

        Log.i("msg",imagePath);
        final BmobFile imageFile = new BmobFile(new File(imagePath));
       imageFile.uploadblock(new UploadFileListener() {
           @Override
           public void done(BmobException e) {
               if (e==null){
                   Log.i("msg","上传成功");
                   saveR(imageFile);
               }else {
                   Log.i("msg","上传失败"+e.getMessage());
               }
           }
       });

    }

    private void saveR(BmobFile imageFile) {
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String f_name=userInfo.getUsername();
        mRelease.setUsername(f_name);

        String s_title=f_title.getText().toString();
        mRelease.setTitle(s_title);
        String s_price=f_price.getText().toString();
        mRelease.setPrice(s_price);

        String s_phone=f_phone.getText().toString();
        mRelease.setPhone(s_phone);
        String s_describe=f_describe.getText().toString();
        mRelease.setDescribe(s_describe);
        mRelease.setImages(imageFile);
        mRelease.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("发布成功",s);
                    SYSDiaLogUtils.showSuccessDialog(ReleaseActivity.this, "二手消息发布成功", "恭喜你，操作成功了！", "OK", false);

                }else {
                    Log.i("失败",e.getMessage());
                    SYSDiaLogUtils.showErrorDialog(ReleaseActivity.this, "失败警告", "很抱歉，这次更新失败，請检查网络重新試試！", "取消", false);

                }
            }
        });

    }

}
