package com.rocedar.deviceplatform.unit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @author liuyi
 * @date 2017/1/12
 * @desc 获取通讯录联系人和打开短信
 * @veison
 */

public class RCPhoneContactUtil {
    /**
     * 获取通讯录联系人
     */
    //获取联系人电话
    public static String getContactPhone(Context context, Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        //System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }


    /**
     * @param mContext
     * @param smsBody  短信内容
     * @param phone    不选择联系人可填 ""
     */

    public static void sendMessage(Context mContext, String smsBody, String phone) {
        Intent intent;
        if ("".equals(phone)) {
            //不知道发送人的电话
            intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("sms_body", smsBody);
            intent.setType("vnd.android-dir/mms-sms");
        } else {
            //知道发送人的电话
            Uri smsToUri = Uri.parse("smsto:" + phone);
            intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", smsBody);
        }
        mContext.startActivity(intent);

    }
}
