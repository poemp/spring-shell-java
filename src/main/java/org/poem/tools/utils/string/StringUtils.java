package org.poem.tools.utils.string;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

/**
 * Created by poem on 2015/2/2.
 * String 字符验证
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

    
    /**
     * 过滤掉文字中的符号
     * @param str
     * @return
     */
    public static String SQLPreProcess(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("'", "&apos;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }

    /**
     * 把转移符转化成标准的符号
     * @param str
     * @return
     */
    public static String disTransformHtml(String str) {
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&apos;", "'");
        str = str.replaceAll("&quot;", "\"");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        return str;
    }
    /**
     * 转换bytes为十六进制字符
     * @param bytes
     * @return 十六进制字符
     */
    public static String bytes2HexString(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bytes.length*2);
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            byte2HexString(sb, bytes[i]);
        }
        return sb.toString();
    }


    /**
     * 转换bytes为十六进制字符
     * @param sb
     * @param b
     */
    public static void byte2HexString(StringBuffer sb, byte b) {
        Validate.notNull(sb);
        transfer2HexStr(sb, (byte) (b >>> 4&0xF));
        transfer2HexStr(sb, (byte) (b&0xF));
    }

    /**
     * 转为为8进制文件
     * @param sb
     * @param b
     */
    private static void transfer2HexStr(StringBuffer sb, byte b) {
        if (b > (byte) 9 && b < (byte) 16) {
            switch (b) {
                case (byte) 10:
                    sb.append('A');
                    break;
                case (byte) 11:
                    sb.append('B');
                    break;
                case (byte) 12:
                    sb.append('C');
                    break;
                case (byte) 13:
                    sb.append('D');
                    break;
                case (byte) 14:
                    sb.append('E');
                    break;
                case (byte) 15:
                    sb.append('F');
                    break;
                default:
            }
        } else {
            sb.append(b);
        }
    }

    /**
     * 段参数
     *
     * @param name
     * @return
     */
    public  static  String shortName(String name) {
        return StringUtils.isNotBlank(name) ? name.substring(0, 1) : "";
    }
}
