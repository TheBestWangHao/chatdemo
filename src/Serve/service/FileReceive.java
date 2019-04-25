package Serve.service;

import tooluse.GetTime;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceive {
    ServerSocket serverSocket;
    Socket clientScoket;
    InputStream inputStream;
    OutputStream outputStream;
    String sendUser_id,revUser_id;
    String fileName,fileType,filePath;
    public static final String rootPath="E:\\FileRev\\";
    DataInputStream dataInputStream;
    BufferedReader bufferedReader;
    FileOutputStream fileOut;
    JTextArea errorLogArea;
    long length;
    int n;
    public static final String splitString="&&&##";
    public FileReceive(Socket clientScoket,JTextArea errorLogArea){
        this.clientScoket=clientScoket;
        this.errorLogArea=errorLogArea;
        try {
            dataInputStream = new DataInputStream(clientScoket.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(clientScoket.getInputStream()));
        }catch (IOException e) {
            errorLogArea.append(GetTime.getTime() + e.getMessage());
            System.out.println("传输文件中客户端获取输入流失败");
        }
        try {
            sendUser_id = bufferedReader.readLine();
            revUser_id = bufferedReader.readLine();
            fileName = bufferedReader.readLine();
            fileType = bufferedReader.readLine();
            filePath = rootPath+splitString+GetTime.getTimeTag() + splitString + sendUser_id + splitString+ revUser_id + splitString + fileName + "." + fileType;
            fileOut = new FileOutputStream(filePath);
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime() + e.getMessage());
            System.out.println("文件模块读取失败");
        }
    }
    public void receiveFile(){
        try {
            length = dataInputStream.readInt();
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime() + e.getMessage());
            System.out.println("文件大小读取失败");
        }
        try {
            byte[] data = new byte[512];
            n = dataInputStream.read(data);
            while (n != -1) {
                fileOut.write(data);
                n = dataInputStream.read(data);
            }
            close();
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime() + e.getMessage());
            System.out.println("文件数据读取失败");
        }
    }
    public void close(){
        try {
            dataInputStream.close();
        } catch (IOException e) {
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
        try{
            bufferedReader.close();
        } catch (IOException e) {
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
        try {
            fileOut.close();
        }catch (IOException  e){
            errorLogArea.append(GetTime.getTime() + e.getMessage());
        }
    }
}
