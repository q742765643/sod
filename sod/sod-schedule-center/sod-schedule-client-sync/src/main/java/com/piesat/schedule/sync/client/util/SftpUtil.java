package com.piesat.schedule.sync.client.util;

import com.jcraft.jsch.*;
import com.piesat.common.utils.OwnException;
import com.piesat.util.ResultT;
import com.piesat.schedule.sync.client.vo.SftpConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author cwh
 * @date 2020年 11月16日 11:57:54
 */
public class SftpUtil {
    private SftpConfig stfpConfig;

    public void setStfpConfig(SftpConfig value) {
        this.stfpConfig = value;
    }

    /**
     * 设置第一次登陆的时候提示key
     */
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    /**
     * 设置第一次登陆的时候提示的值，可选值：(ask | yes | no)
     */
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING_VALUE = "no";

    private ChannelSftp sftp = null;

    /**
     * 创建SFTP连接
     *
     * @throws Exception 抛出异常
     */
    public void createSftp() throws Exception {
        JSch jsch = new JSch();

        Session session = createSession(jsch, stfpConfig.getHostname(), stfpConfig.getUsername(), stfpConfig.getPort());
        session.setPassword(stfpConfig.getPassword());
        session.connect(stfpConfig.getSessionConnectTimeout());


        Channel channel = session.openChannel(stfpConfig.getProtocol());
        channel.connect(stfpConfig.getChannelConnectedTimeout());

        this.sftp = (ChannelSftp) channel;
    }

    /**
     * 创建session
     *
     * @param jsch     jsch
     * @param host     连接地址
     * @param username 用户名
     * @param port     端口号
     * @throws Exception 抛出异常
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws Exception {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }

        if (session == null) {
//            log.error(host + " session is null");
        }

//        Properties config = new Properties();
//        config.put("cipher.s2c", "aes192-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour128,arcfour256");
//        config.put("cipher.c2s", "aes192-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour128,arcfour256");
//        config.put("kex", "diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521");
//        config.put("mac.s2c", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");
//        config.put("mac.c2s", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, SESSION_CONFIG_STRICT_HOST_KEY_CHECKING_VALUE);
        return session;
    }

    /**
     * 关闭连接
     */

    public void disconnect() {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
//                    log.info("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
//            log.error("disconnect failure. msg:{}", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * 生成文件夹
     *
     * @param dirPath 文件夹目录
     */
    private boolean createDirs(String dirPath) {
        if (dirPath != null && !dirPath.isEmpty() && sftp != null) {
            String[] dirs = Arrays.stream(dirPath.split(File.separator+File.separator))
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);
            try {
                sftp.cd(dirPath);
//                log.info("Change directory {}", dirPath);
            } catch (Exception ex) {
                for (String dir : dirs) {
                    if (dir == null || dir.isEmpty() || "..".equals(dir) || ".".equals(dir)) {
                        continue;
                    }
                    try {
                        sftp.cd(dir);
//                        log.info("Change directory {}", dir);
                    } catch (Exception e) {
                        try {
                            sftp.mkdir(dir);
//                            log.info("Create directory {}", dir);
                        } catch (SftpException e1) {
//                            log.error("Create directory failure, directory:{}", dir, e1);
                            System.out.println(e1.getMessage());
                        }
                        try {
                            sftp.cd(dir);
//                            log.info("Change directory {}", dir);
                        } catch (SftpException e1) {
//                            log.error("Change directory failure, directory:{}", dir, e1);
                            System.out.println(e1.getMessage());
                        }
                    }
                }
            }
            return true;
        }
        return true;
    }

    /**
     * 查询目录下的文件
     *
     * @param targetPath 目录
     * @return List<String>    目录名称列表
     */
    public List<String> getFiles(String targetPath) {
        List<String> ret = new ArrayList<>();
        try {
            if (null == sftp) {
                return ret;
            }
            sftp.cd(targetPath);
            Vector<String> files = sftp.ls("*");
            for (int i = 0; i < files.size(); i++) {
                Object obj = files.elementAt(i);
                if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                    ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;
                    if (!entry.getAttrs().isDir()) {
                        ret.add(entry.getFilename());
                    }
                    if (entry.getAttrs().isDir()) {
                        if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                            ret.add(entry.getFilename());
                        }
                    }
                }
            }
        } catch (SftpException e) {
//            log.error("Get files failure. TargetPath: {}", targetPath, e);
            System.out.println(e.getMessage());
        }
        return ret;
    }

    /**
     * 上传文件
     *
     * @param targetPath  文件sftp服务器上的目录
     * @param inputStream 本地文件
     * @throws Exception 抛出异常
     */
    public boolean uploadFile(String targetPath, InputStream inputStream, ResultT<String> resultT) {
        try {
            if (null == sftp) {
                return false;
            }
            sftp.cd(stfpConfig.getRoot());
//            log.info("Change path to {}", stfpConfig.getRoot());

            int index = targetPath.lastIndexOf(File.separator);
            String fileDir = "";
            String fileName = targetPath;
            if (index > 0) {
                fileDir = targetPath.substring(0, index);
                fileName = targetPath.substring(index + 1);
            }

            boolean dirs = this.createDirs(fileDir);
            if (!dirs) {
//                log.error("Remote path error. path:{}", targetPath);
                throw new Exception("Upload File failure");
            }

            sftp.put(inputStream, fileName, ChannelSftp.OVERWRITE);
            return true;
        } catch (Exception e) {
//            log.error("Upload file failure. TargetPath: {}", targetPath, e);
            resultT.setErrorMessage("上传到文件{}失败，错误： {}", targetPath, OwnException.get(e));
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
//                log.error("InputStream close failure. TargetPath: {}", targetPath, ex);
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * 上传文件
     *
     * @param targetPath 文件sftp服务器上的目录
     * @param file       本地文件
     */
    public boolean uploadFile(String targetPath, File file, ResultT<String> resultT) {
        try {
            return this.uploadFile(targetPath, new FileInputStream(file), resultT);
        } catch (FileNotFoundException ex) {
//            log.error("update file failure. TargetPath: {}", targetPath, ex);
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param targetPath 文件sftp服务器上的目录
     */
    public File downloadFile(String targetPath) {
        OutputStream outputStream = null;
        try {
            if (null == sftp) {
                return null;
            }
            sftp.cd(stfpConfig.getRoot());
            File file = new File(targetPath.substring(targetPath.lastIndexOf(File.separator) + 1));

            outputStream = new FileOutputStream(file);
            sftp.get(targetPath, outputStream);
            return file;
        } catch (Exception e) {
//            log.error("Download file failure. TargetPath: {}", targetPath, e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
//                log.error("close output stream failure. {}", ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * 删除文件
     *
     * @param targetPath 文件sftp服务器上的目录
     */
    public boolean deleteFile(String targetPath) {
        try {
            if (null == sftp) {
                return false;
            }
            sftp.cd(stfpConfig.getRoot());
            sftp.rm(targetPath);
            return true;
        } catch (SftpException e) {
//            log.error("Delete file failure. TargetPath: {}", targetPath, e);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ChannelSftp getSftp() {
        return this.sftp;
    }
}
