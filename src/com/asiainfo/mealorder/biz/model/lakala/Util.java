package com.asiainfo.mealorder.biz.model.lakala;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by gjr on 2016/8/23 12:12.
 * mail : gjr9596@gmail.com
 */
public class Util {


    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


    /**
     * 将16进制字符串转换为byteArray
     *
     * @param hexString
     *            the HEX string.
     * @return the byte array.
     */
    public static byte[] toByteArray(String hexString) {

        int hexStringLength = hexString.length();
        byte[] byteArray = null;
        int count = 0;
        char c;
        int i;

        // Count number of hex characters
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        boolean first = true;
        int len = 0;
        int value;
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[len] = (byte) (value << 4);

                } else {

                    byteArray[len] |= value;
                    len++;
                }

                first = !first;
            }
        }

        return byteArray;
    }

    /**
     * Converts the byte array to HEX string.
     *
     * @param buffer
     *            the buffer.
     * @return the HEX string.
     */
    public static String toHexString(byte[] buffer) {

        String bufferString = "";

        for (int i = 0; i < buffer.length; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            bufferString += hexChar.toUpperCase() + " ";
        }

        return bufferString;
    }

}
