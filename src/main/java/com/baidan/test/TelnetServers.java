package com.baidan.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-10-15 上午10:25:31 类说明
 */
public class TelnetServers {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: java TelentServets srcFilePath resultFilePath!");
        }
        new TelnetServers().telnetServerList(args[0], args[1]);
    }

    /**
     * 测试源文件当中的ip和端口是否能够连通
     *
     * @param srcFilePath    源文件
     * @param resultFilePath 结果文件
     */
    private void telnetServerList(String srcFilePath, String resultFilePath) {
        final File srcFile = new File(srcFilePath);
        final File resFile = new File(resultFilePath);

        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = new FileInputStream(srcFile);
            outputStream = new FileOutputStream(resFile);
            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String ipAndPortStr = null;
            while ((ipAndPortStr = br.readLine()) != null) {
                if (ipAndPortStr.isEmpty()) {
                    continue;
                }
                int index = ipAndPortStr.indexOf(" ");
                final String ip = ipAndPortStr.substring(0, index);
                final int port = Integer.parseInt(ipAndPortStr.substring(index + 1));
                final boolean flag = telnetServer(ip, port);
                if (!flag) {
                    ipAndPortStr += "\n";
                    outputStream.write(ipAndPortStr.getBytes("UTF-8"));
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接server
     *
     * @param ip
     * @param port
     * @return 连接成功返回true 失败返回false
     */
    private boolean telnetServer(String ip, int port) {
        Socket socket = null;
        try {
            socket = new Socket(ip, (port));
            if (socket.isConnected()) {
                return true;
            }

        } catch (IOException ex) {
            System.out.println("Connection false ip = " + ip + " port = " + port);
            return false;
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}