package org.poem.tools.utils.file;

import org.apache.commons.io.IOUtils;
import org.poem.tools.utils.collection.Sets;
import org.poem.tools.utils.logger.LoggerUtils;
import org.poem.tools.utils.string.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by poem on 2016/6/18.
 */
public final class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 禁止创建工具类的实例.
     */
    private FileUtils() {
    }

    /**
     * 扫描指定根路径下满足条件的文件的URL地址集合.
     *
     * @param path       文件夹路径
     * @param fileFilter 文件过滤器
     * @return 文件url地址集合
     */
    public static List<URL> scanFileByPath(String path, FileFilter fileFilter) {
        LoggerUtils.info(path);
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        List<URL> rsList = new ArrayList<URL>();
        File rootFile = new File(path);
        if (rootFile.exists() && rootFile.isDirectory()) {
            try {
                scanFile(rsList, rootFile.listFiles(fileFilter), fileFilter);
            } catch (MalformedURLException e) {
                LoggerUtils.error(e.getMessage(), e);
            }
        }
        return rsList;
    }

    /**
     * Scan file.
     *
     * @param urlArray   符合条件的文件的URL地址的集合
     * @param files      被扫描的文件对象数组
     * @param fileFilter 文件过滤器
     * @throws MalformedURLException the malformed url exception
     */
    private static void scanFile(List<URL> urlArray, File[] files, FileFilter fileFilter) throws MalformedURLException {
        if (files != null && urlArray != null && fileFilter != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    urlArray.add(files[i].toURI().toURL());
                } else if (files[i].isDirectory()) {
                    scanFile(urlArray, files[i].listFiles(fileFilter), fileFilter);
                }
            }
        }
    }

    /**
     * @param file
     * @return
     * @throws Exception
     * @Title: read
     * @Description: 读取文件流
     */
    public static byte[] read(File file) {
        if (file != null && file.exists() && file.isFile()) {
            FileInputStream fileInputStream = null;
            try {
                byte[] fileContent = new byte[(int) file.length()];//如果文件太大，可能强转失败，太大的文件内存其实也放不下
                fileInputStream = new FileInputStream(file);
                FileChannel fileChannel = fileInputStream.getChannel();
                ByteBuffer readBuffer = ByteBuffer.allocateDirect(8192);//创建直接缓存区，避免缓冲区复制
                int count = 0;
                while (fileChannel.read(readBuffer) != -1) {
                    readBuffer.flip();//让buffer做好get准备
                    readBuffer.get(fileContent, count, readBuffer.limit());
                    count += readBuffer.position();
                    readBuffer.clear();//让buffer做好put准备
                }
                return fileContent;
            } catch (IOException e) {
                LoggerUtils.error(e.getMessage(), e);
            } finally {
                if (null != fileInputStream) {
                    IOUtils.closeQuietly(fileInputStream);
                }
            }
        }
        return new byte[0];
    }

    /**
     * 获取文件的扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取文件名称
     * @param fileName 文件名称
     * @return
     */
    public static String getFileExistName(String fileName){
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(0,dot);
            }
        }
        return fileName;
    }
    /**
     * window 按照权限写入数据
     *
     * @param path       路径
     * @param byteBuffer 写入的数据
     * @throws IOException
     */
    public static void fileWindowsWritePermission(Path path, ByteBuffer byteBuffer) throws IOException {
        Set<OpenOption> options = new HashSet<>();
        options.add(StandardOpenOption.CREATE_NEW);
        options.add(StandardOpenOption.WRITE);

        UserPrincipalLookupService service = path.getFileSystem().getUserPrincipalLookupService();
        UserPrincipal user = service.lookupPrincipalByGroupName("Uses");
        UserPrincipal systemGroup = service.lookupPrincipalByGroupName("SYSTEM");

        final AclEntry aclEntry = AclEntry
                .newBuilder()
                .setType(AclEntryType.ALLOW)
                .setPrincipal(user)
                .setPermissions(AclEntryPermission.READ_DATA,
                        AclEntryPermission.READ_ATTRIBUTES,
                        AclEntryPermission.READ_NAMED_ATTRS,
                        AclEntryPermission.READ_ACL,
                        AclEntryPermission.WRITE_DATA,
                        AclEntryPermission.WRITE_ATTRIBUTES,
                        AclEntryPermission.WRITE_NAMED_ATTRS,
                        AclEntryPermission.WRITE_ACL,
                        AclEntryPermission.SYNCHRONIZE).build();

        final AclEntry aclEntry1 = AclEntry.newBuilder()
                .setType(AclEntryType.ALLOW)
                .setPrincipal(systemGroup)
                .setPermissions(AclEntryPermission.READ_DATA,
                        AclEntryPermission.READ_ATTRIBUTES,
                        AclEntryPermission.READ_NAMED_ATTRS,
                        AclEntryPermission.WRITE_DATA).build();

        FileAttribute<List<AclEntry>> aclattribute = new FileAttribute<List<AclEntry>>() {
            @Override
            public String name() {
                return "acl:acl";
            }

            @Override
            public List<AclEntry> value() {
                ArrayList<AclEntry> aclEntryArrayList = new ArrayList<>();
                aclEntryArrayList.add(aclEntry);
                aclEntryArrayList.add(aclEntry1);
                return aclEntryArrayList;
            }
        };

        SeekableByteChannel seekableByteChannel = null;
        try {
            seekableByteChannel = Files.newByteChannel(path, options, aclattribute);
            seekableByteChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtils.error(e);
        } finally {
            IOUtils.closeQuietly(seekableByteChannel);
        }
    }


    /**
     * 写入数据
     *
     * @param path       文件的路劲
     * @param byteBuffer 写入数据
     */
    public static void fileLinuxWritePermission(Path path, ByteBuffer byteBuffer) {
        Set<OpenOption> options = Sets.empty();
        options.add(StandardOpenOption.CREATE_NEW);
        options.add(StandardOpenOption.APPEND);
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        SeekableByteChannel sbc = null;
        try {
            sbc = Files.newByteChannel(path, options, attr);
            sbc.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtils.error(e);
        } finally {
            IOUtils.closeQuietly(sbc);
        }

    }
}
