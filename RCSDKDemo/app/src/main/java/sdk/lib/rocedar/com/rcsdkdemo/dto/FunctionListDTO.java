package sdk.lib.rocedar.com.rcsdkdemo.dto;

import android.view.View;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/17 上午11:04
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class FunctionListDTO {


    public FunctionListDTO(String functionName) {
        this.functionName = functionName;
        this.clickListener = null;
        this.head = true;
    }

    public FunctionListDTO(String functionName, View.OnClickListener clickListener) {
        this.functionName = functionName;
        this.clickListener = clickListener;
        this.head = false;
    }

    public String functionName;

    public View.OnClickListener clickListener;

    public boolean head;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
        this.head = head;
    }
}
