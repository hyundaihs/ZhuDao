package com.hzncc.zhudao.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hzncc.zhudao.R;
import com.hzncc.zhudao.ZDApplication;
import com.hzncc.zhudao.db.UserDao;
import com.hzncc.zhudao.entity.User;

import java.util.List;

/**
 * ZhuDao
 * Created by 蔡雨峰 on 2017/3/14.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String[] names = {"kevin", "lucy", "tom", "lily", "jack"};
    private static final int[] numbers = {1, 2, 3, 4, 5};
    private ImageView userImage, psdImage;
    private EditText userInput, psdInput;
    private Button forget, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        putUserInfo();
    }

    private void initViews() {
        userImage = (ImageView) findViewById(R.id.input_image_user);
        userImage.setImageResource(R.mipmap.login_user);
        userInput = (EditText) findViewById(R.id.input_Edit_user);
        userInput.setHint("请输入姓名/编号");
        psdImage = (ImageView) findViewById(R.id.input_image_pwd);
        psdImage.setImageResource(R.mipmap.login_pd);
        psdInput = (EditText) findViewById(R.id.input_Edit_pwd);
        psdInput.setHint("请输入4位数密码");
        psdInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        psdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        forget = (Button) findViewById(R.id.activity_login_button_forget);
        login = (Button) findViewById(R.id.activity_login_button_login);
        forget.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_button_forget:
                break;
            case R.id.activity_login_button_login:
                login();
                startActivity(new Intent(this, HomeActivity.class));
                //finish();
//                String result = checkEdits();
//                if (result == null) {
//                    if (login()) {
//                        startActivity(new Intent(this, HomeActivity.class));
//                        finish();
//                    } else {
//                        DialogUtil.showErrorDialog(this, "用户名或密码不正确");
//                    }
//                } else {
//                    DialogUtil.showErrorDialog(this, result);
//                }
                break;
            default:
                break;
        }
    }

    private String checkEdits() {
        if (isEmpty(userInput)) {
            return "用户名或编号不能为空";
        } else if (isEmpty(psdInput)) {
            return "密码不能为空";
        } else {
            return null;
        }
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void putUserInfo() {
        UserDao userDao = new UserDao(ZDApplication.dbManager.getDb());
        List<User> list = userDao.querys(User.class);
        if (list.size() <= 0) {
            for (int i = 0; i < names.length; i++) {
                list.add(new User(0, names[i], numbers[i], "123"));
            }
            userDao.adds(list);
        }
    }

    private boolean login() {
//        User user = new User();
//        String u = userInput.getText().toString();
//        try {
//            int i = Integer.parseInt(u);
//            user.setNumber(i);
//            user.setName("");
//        } catch (NumberFormatException e) {
//            user.setName(u);
//        } finally {
//            user.setPassword(psdInput.getText().toString());
//            UserDao userDao = new UserDao(ZDApplication.dbManager.getDb());
//            boolean result = userDao.login(user);
//            if (result) {
//                ZDApplication.setLoger(user);
//                WorkLogDao workLogDao = new WorkLogDao(ZDApplication.dbManager.getDb());
//                workLogDao.createWorkLog(user.getName(), WorkLog.EVENTS[0]);
//            }
//            return result;
//        }
        User user = new User();
        user.setNumber(1);
        user.setId(1);
        user.setName("kevin");
        user.setPassword("123");
//        ZDApplication.setLoger(user);
//        WorkLogDao workLogDao = new WorkLogDao(ZDApplication.dbManager.getDb());
//        workLogDao.createWorkLog(user.getName(), WorkLog.EVENTS[0]);
        return true;
    }


    private String getString(char c) {
        int intC = (int) c;
        String s = String.valueOf(intC);
        return s;
    }
}
