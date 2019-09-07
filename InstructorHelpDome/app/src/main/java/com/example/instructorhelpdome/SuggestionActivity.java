package com.example.instructorhelpdome;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.instructorhelpdome.JavaBean.MyUser;
import com.example.instructorhelpdome.JavaBean.Suggestion;
import com.fingerth.supdialogutils.SYSDiaLogUtils;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SuggestionActivity extends AppCompatActivity  implements View.OnClickListener  {
    private EditText theme;
    private EditText advise;
    private Button btn_sugg;
    private ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        init();

    }

    private void init() {
        theme=(EditText)findViewById(R.id.theme);
        advise=(EditText)findViewById(R.id.advise);
        btn_sugg=(Button)findViewById(R.id.btn_suggest);
        btn_sugg.setOnClickListener(this);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_suggest:
                insertSugg();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void insertSugg() {
        Suggestion msuggestion=new Suggestion();
        String stheme=theme.getText().toString();
        String sadvise=advise.getText().toString();
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String f_name=userInfo.getUsername();
        msuggestion.setUsername(f_name);
        msuggestion.setTheme(stheme);
        msuggestion.setAdvise(sadvise);
        msuggestion.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.i("插入成功",s);
                    SYSDiaLogUtils.showSuccessDialog(SuggestionActivity.this, "提交成功", "感谢您的建言，学校会越来越好!", "OK", false);

                }else {
                    Log.i("失败",e.getMessage());
                    SYSDiaLogUtils.showErrorDialog(SuggestionActivity.this, "失败警告", "很抱歉，这次更新失败，請检查网络重新試試！", "取消", false);

                }
            }
        });
    }
}
