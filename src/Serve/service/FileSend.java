package Serve.service;

import tooluse.GetTime;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class FileSend {
    String sender_id, receiver_id,receiver_ip;
    String fileName, fileType, filePath;
    Long fileLength;
    Socket socket;
    JTextArea errorLogArea;
    String splitString = FileReceive.splitString;
    FileInputStream fileInputStream;
    BufferedWriter bufferedWriter;
    OutputStream outputStream;
    String timeTag;
    File file;
    int n;

    public FileSend(String receiver_ip,String[] fileInfo,JTextArea errorLogArea) {
        file = new File(filePath);
        timeTag=fileInfo[0];
        this.sender_id = fileInfo[1];
        receiver_id=fileInfo[2];
        fileName=fileInfo[3];
        fileType=fileInfo[4];
        filePath=fileInfo[5];
        this.receiver_ip = receiver_ip;
        this.errorLogArea = errorLogArea;
        this.filePath = filePath;
        getFileInfo();
    }

    public void getFileInfo() {
        /*String allFileName = file.getName();
        String[] fileInfo = allFileName.split(splitString);
        timeTag=fileInfo[0];
        sender_id = fileInfo[1];
        receiver_ip = fileInfo[2];
        fileName = fileInfo[3];
        String[] types = allFileName.split("\\.");
        fileType = types[types.length - 1];*/
    }

    public void sendFile() {
        try {
            socket = new Socket(receiver_ip, 9500);
        } catch (IOException e) {
            System.out.println("Socket连接建立失败");
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("文件流创建失败");
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
        try {
            outputStream = socket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(timeTag);
            bufferedWriter.newLine();
            bufferedWriter.write(sender_id);
            bufferedWriter.newLine();
            bufferedWriter.write(fileName);
            bufferedWriter.newLine();
            bufferedWriter.write(fileType);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            byte[] data = new byte[512];
            n = fileInputStream.read(data);
            while ((n = fileInputStream.read(data)) != -1) {
                outputStream.write(data, 0, n);
            }
        } catch (IOException e) {
            System.out.println("文件发送失败");
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
            bufferedWriter.close();
            fileInputStream.close();
        } catch (IOException e) {
            errorLogArea.append(GetTime.getTime() + e.getMessage());
            System.out.println("文件发送模块中，关闭流或者Socket异常");
        }
    }
}
