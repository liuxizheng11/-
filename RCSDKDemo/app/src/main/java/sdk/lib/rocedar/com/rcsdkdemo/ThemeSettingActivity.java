package sdk.lib.rocedar.com.rcsdkdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rocedar.lib.base.manage.RCBaseActivity;

import java.lang.reflect.Field;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 上午10:47
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class ThemeSettingActivity extends RCBaseActivity {

    public static void goActivity(Context context) {
        Intent intent = new Intent(context, ThemeSettingActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);
        mRcHeadUtil.setTitle("主题设置");

        groupColor = findViewById(R.id.group_color);
        groupRaduis = findViewById(R.id.group_radius);
        groupTitleBg = findViewById(R.id.group_title_bg);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < groupColor.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) groupColor.getChildAt(i);
                    if (rb.isChecked()) {
                        colorChoose = i;
                        break;
                    }
                }
                for (int i = 0; i < groupRaduis.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) groupRaduis.getChildAt(i);
                    if (rb.isChecked()) {
                        raduisChoose = i;
                        break;
                    }
                }
                for (int i = 0; i < groupTitleBg.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) groupTitleBg.getChildAt(i);
                    if (rb.isChecked()) {
                        titleBgChoose = i;
                        break;
                    }
                }

                change();
            }
        });




    }


    private RadioGroup groupColor;
    private RadioGroup groupTitleBg;
    private RadioGroup groupRaduis;

    private int colorChoose;
    private int titleBgChoose;
    private int raduisChoose;


    /**
     * 该方法仅提供demo中查看
     * 请在AndroidManifest.xml 中<application下android:theme的样式中添加对应的配置项（如果不添加，运行会报错）
     */
    public void change() {
        int themes = R.style.AppTheme_Blue_White_0;
        StringBuffer themeName = new StringBuffer();
        themeName.append("AppTheme_");
        switch (colorChoose) {
            case 0:
                themeName.append("Blue_");
                break;
            case 1:
                themeName.append("Purple_");
                break;
            case 2:
                themeName.append("Gray_");
                break;
        }
        switch (titleBgChoose) {
            case 0:
                themeName.append("White_");
                break;
            case 1:
                themeName.append("Black_");
                break;
        }
        switch (raduisChoose) {
            case 0:
                themeName.append("0");
                break;
            case 1:
                themeName.append("5");
                break;
        }
//        RCBaseConfig.themes = getResource(themeName.toString()) != 0 ? getResource(themeName.toString()) : themes;
    }


    public int getResource(String themeName) {
        Class mipmap = R.style.class;
        try {
            Field field = mipmap.getField(themeName);
            int resId = field.getInt(themeName);
            return resId;
        } catch (NoSuchFieldException e) {
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }

    }

}
