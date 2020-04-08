//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.rocedar.deviceplatform.device.bluetooth.impl.yd;

import com.rocedar.deviceplatform.device.bluetooth.impl.DoBluetoothBleUtilYDImpl;
import com.rocedar.deviceplatform.device.bluetooth.impl.RCBluetoothYDImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Funtion {
    Calendar cal = Calendar.getInstance(Locale.getDefault());
    int zoneOffset;

    public Funtion() {
        this.zoneOffset = this.cal.get(Calendar.ZONE_OFFSET) / 1000;
    }

    public void getstepdata() {
        int i;
        String string;
        String change;
        for (i = 0; i < Array.liststring.size(); ++i) {
            string = (String) Array.liststring.get(i);
            change = string.substring(0, string.length() - 3);
            String step = change.substring(3, 5);
            if ("01".equals(step)) {
                if (i < Array.liststring.size() - 1) {
                    String time1 = (String) Array.liststring.get(i + 1);
                    String second = time1.substring(0, time1.length() - 3);
                    String time2 = second.substring(3, 5);
                    if ("02".equals(time2)) {
                        StringBuffer total = new StringBuffer(second);
                        total.delete(0, 6);
                        String time3 = total.toString();
                        String total1 = change + time3;
                        Array.listafterstring.add(total1);
                    } else {
                        Array.listafterstring.add(change);
                    }
                } else if (i == Array.liststring.size() - 1) {
                    Array.listafterstring.add(change);
                }
            }
        }

        for (i = 0; i < Array.listafterstring.size(); ++i) {
            string = (String) Array.listafterstring.get(i);
            change = string.toString().replace(" ", "").toUpperCase();
            byte[] var26 = Utils.hexStringToByte(change);
            long var27 = (long) (var26[2] & 255);
            long var28 = (long) (var26[3] & 255);
            long var29 = (long) (var26[4] & 255);
            long time4 = (long) (var26[5] & 255);
            long aa = time4 + (var29 << 8) + (var28 << 16) + (var27 << 24) - (long) this.zoneOffset;
            SimpleDateFormat foo = new SimpleDateFormat("yyyyMMddHHmmss");

            for (int j = 6; j < var26.length; j += 2) {
                StepData stepData = new StepData();
                int stepsum = var26[j] & 255;
                int caloriesum = 0;
                if (var26.length > j + 1)
                    caloriesum = var26[j + 1] & 255;
                long xx = aa + (long) (60 * (j - (j / 2 + 3)));
                long ss = xx * 1000L;
                Date date = new Date(ss);
                String time = foo.format(Long.valueOf(date.getTime()));
                stepData.setSteptime(time);
                stepData.setStepdata(stepsum);
                stepData.setCaloriedata(caloriesum);
                Array.liststep.add(stepData);
            }
        }

    }

    public void getsleepdata() {
        int i;
        String string;
        String change;
        for (i = 0; i < Array.listsleepstring.size(); ++i) {
            string = (String) Array.listsleepstring.get(i);
            change = string.substring(0, string.length() - 3);
            Array.listsleepafterstring.add(change);
        }

        for (i = 0; i < Array.listsleepafterstring.size(); ++i) {
            string = (String) Array.listsleepafterstring.get(i);
            change = string.toString().replace(" ", "").toUpperCase();
            byte[] sleep = Utils.hexStringToByte(change);
            long time1 = (long) (sleep[2] & 255);
            long time2 = (long) (sleep[3] & 255);
            long time3 = (long) (sleep[4] & 255);
            long time4 = (long) (sleep[5] & 255);
            long aa = time4 + (time3 << 8) + (time2 << 16) + (time1 << 24) - (long) this.zoneOffset;
            SimpleDateFormat foo = new SimpleDateFormat("yyyyMMddHHmmss");

            for (int j = 6; j < sleep.length; ++j) {
                SleepData sleepData = new SleepData();
                int sleepsum = sleep[j] & 255;
                long xx = aa + (long) (300 * (j - 6));
                long ss = xx * 1000L;
                Date date = new Date(ss);
                String time = foo.format(Long.valueOf(date.getTime()));
                sleepData.setSleeptime(xx);
                sleepData.setSleepdata(sleepsum);
                sleepData.setIsupdate(0);
                int hour = Integer.valueOf(time.substring(11, 13)).intValue();
                if (hour <= 12) {
                    sleepData.setMarktime(time.substring(0, 10));
                } else {
                    ss += 43200000L;
                    Date date1 = new Date(ss);
                    String timehour = foo.format(Long.valueOf(date1.getTime()));
                    sleepData.setMarktime(timehour.substring(0, 10));
                }

                Array.listsleep.add(sleepData);
            }
        }

    }


    public void sendWriteData(DoBluetoothBleUtilYDImpl rcBluetoothBYD) {
        String aa = "E1 00 03 4A 1E AA 14 01 00 27 10 42";
        String bb = aa.toString().replace(" ", "");
        byte[] cc = Utils.hexStringToByte(bb);
        rcBluetoothBYD.writeData(cc);
    }

    public void setdata(String value,DoBluetoothBleUtilYDImpl rcBluetoothBYD,
                        RCBluetoothYDImpl bluetoothYD) {
        String tt = value.toString().replace(" ", "").toUpperCase();
        byte[] data = Utils.hexStringToByte(tt);
        if (data[0] == 1) {
            String aa = "E1 00 03 4A 1E AA 14 01 00 27 10 42";
            String bb = aa.toString().replace(" ", "");
            byte[] cc = Utils.hexStringToByte(bb);
            rcBluetoothBYD.writeData(cc);
        }

        int var20;
        int var22;
        if ((data[0] & 15) == 2) {
            var20 = 0;
            boolean var21 = false;
            Calendar var23 = Calendar.getInstance();
            long dd = var23.getTimeInMillis() / 1000L + (long) this.zoneOffset;
            long checksum = dd / 16777216L;
            long shuju = dd % 16777216L / 65536L;
            long j = dd % 65536L / 256L;
            long time1 = dd % 256L;
            byte[] newdata = new byte[]{(byte) -30, (byte) 1, (byte) ((int) checksum), (byte) ((int) shuju), (byte) ((int) j), (byte) ((int) time1), (byte) 0};

            for (int j1 = 0; j1 <= newdata.length - 2; ++j1) {
                var22 = newdata[j1];
                if (var22 < 0) {
                    var22 += 256;
                }

                var20 += var22;
            }

            newdata[6] = (byte) (var20 & 255);
            rcBluetoothBYD.writeData(newdata);
        }

        int ee;
        byte[] newdata1;
        int var24;
        int var31;
        if ((data[0] & 15) == 3) {
            var20 = data[5] & 255;
            var22 = data[6] & 255;
            var24 = data[7] & 255;
            Array.totaldata = var24 + (var22 << 8) + (var20 << 16);
            Array.totaldistance = data[10] & 255 + ((data[9] & 255) << 8) + ((data[8] & 255) << 16);
            int var27 = data[11] & 255;
            ee = data[12] & 255;
            var31 = data[13] & 255;
            Array.totalcalorie = (var31 + (ee << 8) + (var27 << 16)) / 10;
            String first = "E0 02 00 E2";
            String var35 = first.toString().replace(" ", "");
            newdata1 = Utils.hexStringToByte(var35);
            rcBluetoothBYD.writeData(newdata1);
        }

        if ((data[0] & 15) == 4 && (data[1] & 15) == 1) {
            Array.liststring.add(value);
        }

        if ((data[0] & 15) == 4 && (data[1] & 15) == 2) {
            Array.liststring.add(value);
        }

        boolean var26;
        byte[] var28;
        if ((data[0] & 15) == 4 && (data[1] & 15) == 3) {
            var20 = 0;
            var22 = (data[0] & 240) >> 4;
            var26 = false;
            var28 = new byte[]{(byte) -32, (byte) var22, (byte) 0, (byte) 0};

            for (ee = 0; ee <= var28.length - 2; ++ee) {
                var24 = var28[ee];
                if (var24 < 0) {
                    var24 += 256;
                }

                var20 += var24;
            }

            var28[3] = (byte) (var20 & 255);
            rcBluetoothBYD.writeData(var28);
        }

        if ((data[0] & 15) == 5 && (data[1] & 15) == 1) {
            Array.listsleepstring.add(value);
        }

        if ((data[0] & 15) == 5 && (data[1] & 15) == 3) {
            var20 = 0;
            var22 = (data[0] & 240) >> 4;
            var26 = false;
            var28 = new byte[]{(byte) -32, (byte) var22, (byte) 0, (byte) 0};

            for (ee = 0; ee <= var28.length - 2; ++ee) {
                var24 = var28[ee];
                if (var24 < 0) {
                    var24 += 256;
                }

                var20 += var24;
            }

            var28[3] = (byte) (var20 & 255);
            rcBluetoothBYD.writeData(var28);
        }

        if ((data[0] & 15) == 7) {
            byte var25 = data[2];
            byte var29 = data[4];
            byte var32 = data[5];
            byte var30 = data[6];
            byte var33 = data[7];
            Array.peaceendhour = String.valueOf(var29);
            Array.peaceendmin = String.valueOf(var32);
            Array.weekendhour = String.valueOf(var30);
            Array.weekendmin = String.valueOf(var33);
            var31 = 0;
            int var34 = (data[0] & 240) >> 4;
            boolean var36 = false;
            newdata1 = new byte[]{(byte) -28, (byte) var34, (byte) 1, (byte) 21, (byte) 30, (byte) 8, (byte) 0, (byte) 9, (byte) 30, (byte) 10, (byte) 60, (byte) 8, (byte) 48, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

            for (int var38 = 0; var38 <= newdata1.length - 2; ++var38) {
                int var37 = newdata1[var38];
                if (var37 < 0) {
                    var37 += 256;
                }

                var31 += var37;
            }

            newdata1[19] = (byte) (var31 & 255);
            rcBluetoothBYD.writeData(newdata1);
        }

        if ((data[0] & 15) == 6) {
//            rcBluetoothBYD.doDisconnect();
            bluetoothYD.parsingData();
        }

    }


}
