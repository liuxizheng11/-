package sdk.lib.rocedar.com.rcsdkdemo.rcconfig;

import android.app.Activity;

import com.rocedar.lib.base.config.IWebViewBaseUtil;
import com.rocedar.lib.sdk.share.share.ShareDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：瑰柏SDK-健康服务（家庭医生）
 * <p>
 * 作者：phj
 * 日期：2018/7/27 下午4:55
 * 版本：V1.1.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class WebViewConfig implements IWebViewBaseUtil {

    @Override
    public Map<String, String> addHeadInfo() {
        return new HashMap<>();
    }

    @Override
    public void jsShare(Activity mContext, String shareInfo) {
        new ShareDialog(mContext, shareInfo).show();
    }
}
