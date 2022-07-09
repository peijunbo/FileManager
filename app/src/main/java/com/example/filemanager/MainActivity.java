package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    TextView curPath;
    File curParent;
    File[] curFiles;
    File root;
    RecyclerView recyclerView;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkPermission()) {
            root = getRoot();
            if (root != null) {
                curParent = root;
                curFiles = curParent.listFiles();
                setRecyclerView();
                Toast.makeText(MainActivity.this, "获取文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "获取文件失败", Toast.LENGTH_SHORT).show();
            }
            setTitle();
        }
        else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermission()) {
            //重新启动
            root = getRoot();
            if (root != null) {
                curParent = root;
                curFiles = curParent.listFiles();
                setRecyclerView();
                Toast.makeText(MainActivity.this, "获取文件成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "获取文件失败", Toast.LENGTH_SHORT).show();
            }
            setTitle();
        }
    }

    private boolean checkPermission() {
        int isPermitted = ActivityCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[0]);
        if (isPermitted != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else {
            return true;
        }
    }

    private File getRoot() {
        File ret;
        boolean isLoadSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isLoadSDCard) {
            ret = Environment.getExternalStorageDirectory();
        }
        else {
            ret = null;
        }
        return ret;
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.file_list);
        FileAdapter adapter;
        adapter = new FileAdapter(curFiles);
        adapter.setOnFileClickListener(new FileAdapter.onFileClickListener() {
            @Override
            public void onFileClick(View view, int position) {
                File[] temp = curFiles[position].listFiles();
                if (temp == null || temp.length==0) {
                    Toast.makeText(MainActivity.this, "当前文件夹没有内容或不能被访问。", Toast.LENGTH_SHORT).show();
                }
                else {
                    TextView title = (TextView) findViewById(R.id.title_text);
                    curParent = curFiles[position];
                    curFiles = curParent.listFiles();
                    title.setText(curParent.getAbsolutePath());
                    changeDirectory(curFiles);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void changeDirectory(File[] files) {
        FileAdapter newAdapter = new FileAdapter(curFiles);
        newAdapter.setOnFileClickListener(new FileAdapter.onFileClickListener() {
            @Override
            public void onFileClick(View view, int position) {
                File[] temp = curFiles[position].listFiles();
                if (temp == null || temp.length == 0) {
                    Toast.makeText(MainActivity.this, "当前文件夹没有内容或不能被访问。", Toast.LENGTH_SHORT).show();
                } else {
                    TextView title = (TextView) findViewById(R.id.title_text);
                    curParent = curFiles[position];
                    curFiles = curParent.listFiles();
                    title.setText(curParent.getAbsolutePath());
                    changeDirectory(curFiles);
                }

            }
        });
        recyclerView.setAdapter(newAdapter);
    }

    private void setTitle() {
        Button titleButton = (Button) findViewById(R.id.back);
        titleButton.setBackgroundResource(R.mipmap.ic_launcher_round);
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curParent.getAbsolutePath().equals(root.getAbsolutePath())) {
                    MainActivity.this.finish();
                }
                else{
                    curParent = curParent.getParentFile();
                    curFiles = curParent.listFiles();
                    changeDirectory(curFiles);
                }
            }
        });
    }

}