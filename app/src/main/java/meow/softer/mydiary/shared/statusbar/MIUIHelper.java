package meow.softer.mydiary.shared.statusbar;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MIUIHelper implements IStatusBarFontHelper{
    @Override
    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                Class clazz = window.getClass();
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isFontColorDark) {
                    //set Translucent
                    WindowManager.LayoutParams winParams = window.getAttributes();
                    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    if (isFontColorDark) {
                        winParams.flags |= bits;
                    } else {
                        winParams.flags &= ~bits;
                    }
                    window.setAttributes(winParams);
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
