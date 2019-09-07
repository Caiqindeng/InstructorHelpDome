package com.example.instructorhelpdome.Dao;

import android.content.Context;
public class APKVersionCodeUtils {
    public static int getVersionCodeUtil(Context context){

        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()//拿到package管理者
                    .getPackageInfo(context.getPackageName(),0).versionCode;

        }catch (Exception e){
            e.printStackTrace();
        }
        return versionCode;//返回versionCode
    }

}
