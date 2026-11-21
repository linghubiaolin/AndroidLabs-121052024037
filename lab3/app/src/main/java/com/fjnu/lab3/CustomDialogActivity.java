package com.fjnu.lab3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 实验点 2：自定义布局的 AlertDialog。
 * 关键点：LayoutInflater 加载布局 -> AlertDialog.Builder.setView() -> create() 后手动绑定按钮。
 */
public class CustomDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.btn_show_dialog).setOnClickListener(v -> showSignInDialog());
    }

    private void showSignInDialog() {
        // 1. 把 XML 布局实例化为 View
        View content = LayoutInflater.from(this).inflate(R.layout.dialog_sign_in, null, false);

        // 2. 通过 Builder.setView() 把自定义布局塞进 AlertDialog
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(content)
                .create();

        EditText etUsername = content.findViewById(R.id.et_username);
        EditText etPassword = content.findViewById(R.id.et_password);
        TextView btnCancel = content.findViewById(R.id.btn_cancel);
        TextView btnSignIn = content.findViewById(R.id.btn_sign_in);

        // 3. 自定义按钮必须在 setView 之后通过 findViewById 自行绑定监听器
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "已取消登录", Toast.LENGTH_SHORT).show();
        });

        btnSignIn.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            Toast.makeText(this, "登录成功，欢迎 " + username, Toast.LENGTH_SHORT).show();
        });

        // 4. 去掉系统默认的白色矩形背景，让布局自身的样式完整呈现
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
