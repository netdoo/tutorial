package com.exftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {

        int port = 21;
        String host = "127.0.0.1";
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login("james", "1111");
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        try (InputStream localFileStream = new FileInputStream("C:\\temp\\test.txt")) {
            ftpClient.storeFile("/test.txt", localFileStream);
        } catch (Exception e) {
            logger.error("fail to upload ", e);
        }

        try (OutputStream localFileStream = new FileOutputStream("C:\\temp\\fromRemoteTest.txt")) {
            ftpClient.retrieveFile("/test.txt", localFileStream);
        } catch (Exception e) {
            logger.error("fail to download ", e);
        }

        ftpClient.disconnect();
    }
}

