package org.poem.tools.utils.properties;

import org.poem.tools.utils.collection.CollectionUtils;
import org.poem.tools.utils.file.FileUtils;
import org.poem.tools.utils.file.URLUtil;
import org.poem.tools.utils.logger.LoggerUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by poem on 2016/6/18.
 */
public final class PropertiesUtils {

    /** 加载配置文件的对象. */
    private static Properties prop = new Properties();


    static{
        try {
            List<URL> fileUrls = FileUtils.scanFileByPath(URLUtil.getClassFilePath(PropertiesUtils.class), new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().endsWith(".properties");
                }
            });
            if(!CollectionUtils.isEmpty(fileUrls)){
                for(URL url : fileUrls) {
                    LoggerUtils.info("load properties file | " + url.getPath() );
                    prop.load(url.openStream());
                }
            }else {
                LoggerUtils.warn("there is not has any properties files");
            }

        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtils.error(e.getMessage(), e);
        }
    }

    /**
     * 禁止创建工具类的实例.
     */
    private PropertiesUtils(){
    }

    /**
     * 获取系统当前运行的黑白名单模式.
     *
     * @return Ip过滤模式配置
     */
    public static String getRunMode(){
        return getString("SYS_RUN_MODE", "N");
    }

    /**
     * 获取系统的黑名单列表表达式.
     *
     * @return 系统黑名单配置
     */
    public static String getBlackList(){
        return prop.getProperty("BLACK_LIST");
    }

    /**
     * 获取系统的黑名单列表表达式.
     *
     * @return 系统白名单配置
     */
    public static String getWhiteList(){
        return prop.getProperty("WHITE_LIST");
    }

    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @return 配置文件中name对应的值
     */
    public static String getString(String name){
        return prop.getProperty( name );
    }

    /**
     * 使用预编译，加快速度
     */
    public  static  Pattern pattern = Pattern.compile("[\\d]+");
    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @param def 默认值
     * @return 配置文件中name对应的值
     */
    public static int getInt(String name, int def){
        String value = prop.getProperty( name );
        if(isNumber(value)){
            return Integer.parseInt( value );
        }
        return def;
    }

    /**
     * 获取资源文件中指定属性.
     *
     * @param name 属性名称
     * @param def 默认值
     * @return 配置文件中name对应的值
     */
    public static String getString(String name, String def){
        String value = prop.getProperty( name );
        if(org.apache.commons.lang3.StringUtils.isNotBlank(value)){
            return value;
        }
        return def;
    }

    /**
     * 判断传入的字符串是否为数字.
     *
     * @param str 字符串表达式
     * @return 字符串是否数字
     */
    private static boolean isNumber(String str) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return false;
        }

        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
