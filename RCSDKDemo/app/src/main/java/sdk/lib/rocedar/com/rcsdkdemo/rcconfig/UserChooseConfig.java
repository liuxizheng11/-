package sdk.lib.rocedar.com.rcsdkdemo.rcconfig;

import android.content.Context;

import com.rocedar.lib.base.network.IResponseData;
import com.rocedar.lib.base.network.RCRequestNetwork;
import com.rocedar.sdk.shop.config.IRCGoodsConfig;
import com.rocedar.sdk.shop.config.IRCShopGoodsChooseUserListener;
import com.rocedar.sdk.shop.config.IRCShopGoodsPostUserListener;
import com.rocedar.sdk.shop.dto.RCShopChooseUserDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sdk.lib.rocedar.com.rcsdkdemo.bean.APIBean;
import sdk.lib.rocedar.com.rcsdkdemo.bean.SaveUserBean;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2018/10/11 下午4:50
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class UserChooseConfig implements IRCGoodsConfig {

    @Override
    public boolean getChooseUserList(Context context, final IRCShopGoodsChooseUserListener listener) {
        APIBean bean = new APIBean();
        bean.setActionName("/user/relation/type/3910/");
        RCRequestNetwork.NetWorkGetData(context, bean, RCRequestNetwork.Method.Get, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                List<RCShopChooseUserDTO> dtoList = new ArrayList<>();
                JSONArray array = data.optJSONObject("result").optJSONArray("relations");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    RCShopChooseUserDTO dto = new RCShopChooseUserDTO();
                    dto.setUserPhoneNo(object.optLong("phone"));
                    dto.setUserId(object.optLong("user_id"));
                    dto.setUserNickName(object.optString("relation_name"));
                    dto.setUserTrueName(object.optString("true_name"));
                    dto.setUserCardNo(object.optString("card_id"));
                    dtoList.add(dto);
                }
                listener.getDataSuccess(dtoList);
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
        return true;
    }

    @Override
    public boolean saveUserInfo(Context context, String nickName, String phoneNo, String idCardNo,
                                String trueName, final IRCShopGoodsPostUserListener listener) {
        SaveUserBean bean = new SaveUserBean();
        bean.setActionName("/user/family/");
        bean.setPhone(phoneNo);
        bean.setCheng_hu(nickName);
        if (!idCardNo.equals(""))
            bean.setCard_id(idCardNo);
        if (!trueName.equals(""))
            bean.setTrue_name(trueName);
        RCRequestNetwork.NetWorkGetData(context, bean, RCRequestNetwork.Method.Post, new IResponseData() {
            @Override
            public void getDataSucceedListener(JSONObject data) {
                listener.getDataSuccess(data.optJSONObject("result").optLong("relation_user_id"));
            }

            @Override
            public void getDataErrorListener(String msg, int status) {
                listener.getDataError(status, msg);
            }
        });
        return true;
    }


}
