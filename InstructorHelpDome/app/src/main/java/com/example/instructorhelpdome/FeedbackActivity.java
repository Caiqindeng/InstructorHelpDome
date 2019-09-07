package com.example.instructorhelpdome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instructorhelpdome.JavaBean.Feedback;
import com.fingerth.supdialogutils.SYSDiaLogUtils;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText f_title;
    private EditText f_details;
    private EditText f_contact;

    private Button but_help_feedback;
    private Button Customer_tele;

    private String sproblem;
    private String sdetails;
    private String scontact;

    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initview();

    }

    private void initview() {

        f_title=(EditText)findViewById(R.id.f_title);
        f_details =(EditText)findViewById(R.id.f_details);
        f_contact=(EditText)findViewById(R.id.f_contact);

        but_help_feedback=(Button)findViewById(R.id.but_help_feedback);
        but_help_feedback.setOnClickListener(this);

        Customer_tele=(Button)findViewById(R.id.Customer_tele);
        Customer_tele.setOnClickListener(this);

        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_help_feedback:
                but_feedback();
                break;
            case R.id.Customer_tele:
                String phoneNum="16642712339";
                toTelephone(phoneNum);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void toTelephone(String phoneNum) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        startActivity(intent);
    }

    private void but_feedback() {
        sproblem=f_title.getText().toString();
        sdetails=f_details.getText().toString();
        scontact=f_contact.getText().toString();

        Feedback feedback=new Feedback();
        feedback.setProblem(sproblem);
        feedback.setDetails(sdetails);
        feedback.setContact(scontact);

        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("插入成功",s);
                    SYSDiaLogUtils.showSuccessDialog(FeedbackActivity.this, "反馈成功", "感谢您的反馈,我们会尽快处理您的意见!", "OK", false);

                }else {
                    Log.i("失败",e.getMessage());
                    SYSDiaLogUtils.showErrorDialog(FeedbackActivity.this, "失败警告", "很抱歉，这次更新失败，請检查网络重新試試！", "取消", false);

                }
            }
        });

    }
}
