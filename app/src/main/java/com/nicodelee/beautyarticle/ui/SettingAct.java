package com.nicodelee.beautyarticle.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.nicodelee.beautyarticle.app.APP;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.utils.FileUtil;
import com.nicodelee.beautyarticle.viewhelper.SampleDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by Nicodelee on 15/3/31.
 */
public class SettingAct extends MaterialSettings implements SampleDialog.OnDialogOkClick {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addItem(new HeaderItem(this).setTitle("常用"));

    //        addItem(new CheckboxItem(this, "key1").setTitle("无图模式").setSubtitle("更省流量").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
    //            @Override
    //            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
    //                Toast.makeText(SettingAct.this, "CHECKED: " + isChecked, Toast.LENGTH_SHORT).show();
    //            }
    //        }));

    //        addItem(new DividerItem(this));
    //        addItem(new SwitcherItem(this, "key1a").setTitle("开启文章推送"));
    //        addItem(new DividerItem(this));
    long dataSize = new Select().count().from(ActicleMod.class).count();
    final TextItem clearDataItem = new TextItem(this, "key1");
    addItem(clearDataItem.setTitle("清除数据（重启生效）")
        .setSubtitle(dataSize + "条")
        .setOnclick(new TextItem.OnClickListener() {
          @Override public void onClick(TextItem v) {
            new AlertDialog.Builder(SettingAct.this).setMessage("是否清除数据?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                  }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SettingAct.this, "清除中...", Toast.LENGTH_SHORT).show();
                    new Delete().table(ActicleMod.class);
                    clearDataItem.updateSubTitle(0 + "条");
                  }
                })
                .create()
                .show();
          }
        }));

    long cacheSize =
        FileUtil.getDirectorySize(APP.getInstance().imageLoader.getDiskCache().getDirectory());
    final TextItem clearImageItem = new TextItem(this, "key2");
    addItem(clearImageItem.setTitle("清除图片（重启生效）")
        .setSubtitle(FileUtil.humanReadableByteCount(cacheSize, false))
        .setOnclick(new TextItem.OnClickListener() {
          @Override public void onClick(TextItem v) {
            new AlertDialog.Builder(SettingAct.this).setMessage("是否清除图片?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                  }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SettingAct.this, "清除中...", Toast.LENGTH_SHORT).show();
                    APP.getInstance().imageLoader.clearMemoryCache();
                    APP.getInstance().imageLoader.clearDiskCache();
                    clearImageItem.updateSubTitle(0 + "M");
                  }
                })
                .create()
                .show();
          }
        }));

    //        addItem(new DividerItem(this));
    addItem(new TextItem(this, "key6").setTitle("使用帮助").setOnclick(new TextItem.OnClickListener() {
      @Override public void onClick(TextItem item) {
        SampleDialog.newInstance().show(getSupportFragmentManager(), "dialog1");
      }
    }));

    addItem(new DividerItem(this));
    addItem(new HeaderItem(this).setTitle("关于"));
    addItem(new TextItem(this, "key4").setTitle("作者")
        .setSubtitle("感谢使用@By Nicodelee")
        .setOnclick(new TextItem.OnClickListener() {
          @Override public void onClick(TextItem v) {
            Toast.makeText(SettingAct.this, "感谢使用@By Nicodelee", Toast.LENGTH_SHORT).show();
          }
        }));
  }

  @Override public StorageInterface initStorageInterface() {
    return new PreferencesStorageInterface(this);
  }

  @Override public void onOkClick() {
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
