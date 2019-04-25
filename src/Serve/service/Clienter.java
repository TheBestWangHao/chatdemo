package Serve.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import tooluse.GetTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Statement;
import java.util.*;

/**
 * 类全称：客户端托管对象，没油一个用户接入服务器，就会创建一个这个类。这个类里面封装了所有客户端与服务器之间进行交互的方法
 * 并且完成了大部分本项目的业务逻辑。
 */
public class Clienter {
    private Socket socket;                              //从ClientManger获得的从客户端发来的客户端的Socket对象
    private boolean waitForClient;                      //是否继续监听端口，等待其发送消息
    private ClientManger clientManger;                  //上级的ClientManger的对象
    private DataBaseUser dataBaseUse;                   //从ClientManger传来的DataBaseUser的对象
    private Statement statement;                        //从ClientManger传来的Statement的对象
    private DefaultTableModel defaultTableModel;        //从ClientManger传来的，服务器管理界面的DefaultTableModel表
    private DataInputStream dataInputStream;            //用Socket获得的inputStrem对象构造而来的数据转换读取流
    private InputStream inputStream;                    //从socket获得的字节输入流
    private OutputStream outputStream;                  //从socket获得的字节输出流
    private BufferedInputStream bufferedInputStream;    //利用inputStream构造的字节缓冲输入流
    private BufferedReader bufferedReader;              //利用inputStream构造的字符缓冲输入流
    private BufferedWriter bufferedWriter;              //向客户端发送消息的字节缓存输出流
    private Thread receiveThread;                       //接受信息的线程
    private JTextArea userLoadArea;                     //Manger中的显示用户日志的JTextarea
    private JTextArea errorLogArea;                     //Manger中的显示错误记录的日志
    private Random random;                              //用于生成随机id的对象
    String connectorId;                                 //当用户登录成功后，获取其ID
    String ip;                                          //获取连接到服务器的客户端IP
    String user_id;                                     //当前连接登陆的用户
    public Clienter(Socket socket, ClientManger clientManger, DataBaseUser dataBaseUse,Statement statement, DefaultTableModel defaultTableModel){
        this.socket=socket;
        this.clientManger=clientManger;
        this.dataBaseUse=dataBaseUse;
        this.statement=statement;
        this.defaultTableModel=defaultTableModel;
        this.statement=statement;
        ip=socket.getInetAddress().getHostAddress();
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        dataInputStream=new DataInputStream(inputStream);
        bufferedInputStream=new BufferedInputStream(inputStream);
        bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
        random=new Random();
    }

    /**
     * 开辟线程接收客户端发来的请求的方法，通过客户端发来的暗号，判断其下面将发来的是什么类型的数据，以及需要服务器端做出何种操作
     * 然后调用相应的方法，执行相应的操作
     */
    public void receiveRequest(){
        receiveThread=new Thread(){
            public void run(){
                String request;
                while (waitForClient) {
                    if(socket.isClosed()){
                        clearResource();
                    }
                    try {
                        if ((request = bufferedReader.readLine()) != null) {
                            if (request.equals(HttpAgreement.LOGIN_REQUEST))
                                logIn();
                            else if (request.equals(HttpAgreement.FRIEND_LIST_REQUEST))
                                getFriendMap();
                            else if(request.equals(HttpAgreement.GROUP_LIST_REQUEST))
                                returnGroupMap();
                            else if(request.equals(HttpAgreement.SEARCH_USER_REQUEST))
                                searchUsers_By_id();
                            else if(request.equals(HttpAgreement.SEARCH_GROUP_REQUEST))
                                returnGroupMap();
                            else if (request.equals(HttpAgreement.ISONLINE_REQUEST))
                                isOnline();
                            else if(request.equals(HttpAgreement.CREATE_GROUP_REQUEST))
                                createGroup();
                            else if(request.equals(HttpAgreement.REGISTE_REQUEST))
                                /*registerUser()*/;
                            else if(request.equals(HttpAgreement.DELETE_FRIEDN_REQUEST))
                                deleteFriend();
                            else if(request.equals(HttpAgreement.DELETE_GROUP_REQUEST))
                                deleteGroup();
                            else if(request.equals(HttpAgreement.SEARCH_USER_NAME_REQUEST)){
                                searchUserName();
                            }
                        }
                    }catch (IOException e){
                        errorLogArea.append(GetTime.getTime()+e.getMessage());
                    }
                }

            }
        };
        receiveThread.start();
    }

    /**
     * 当服务器接收到用户的登录请求时，会执行此方法，判断用户是否可以登录。若可以则返回一个允许登录的暗号，告知客户端。
     * 若不可，则返回一个
     */
    public void logIn(){
        String account,password;
        try {
            account = bufferedReader.readLine();
            password = bufferedReader.readLine();
            this.user_id=account;
            Boolean canLogin = dataBaseUse.loginIsRight(account, password);
            if (canLogin) {
                sendMessage(HttpAgreement.CAN_LOGIN_RESPONSE);
                dataBaseUse.setOnlineState(account,ip,true);
                this.connectorId=account;
                sendFile(account);
            } else {
                sendMessage(HttpAgreement.CANNOT_LOGIN_RESPONSE);
            }
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }

    /**
     * 将该用户的好友列表中好友的 账号 密码
     * 发送至客户端   （先发id，再发名字）
     */
    public void getFriendMap(){
        Map<String,String> friends=new HashMap<>();
        try {
            Iterator<Map.Entry<String,String>> it=friends.entrySet().iterator();
            friends = dataBaseUse.getFriends(bufferedReader.readLine());
            while (it.hasNext()){
                Map.Entry<String,String> mapEntry=it.next();
                bufferedWriter.write(mapEntry.getKey());
                bufferedWriter.newLine();
                bufferedWriter.write(mapEntry.getValue());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
    }

    /**
     * 向客户端发送目标ID的群聊名称
     */
    public void returnGroupMap(){
        List<String> groupNameList=new LinkedList<>();
        try {
            groupNameList = dataBaseUse.getGroupChat(bufferedReader.readLine());     //读取用户端发来的id作为方法的参数
            Iterator<String> it=groupNameList.iterator();
            while (it.hasNext()){
                sendMessage(it.next());
            }
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }

    /**
     * 接收客户端传来的聊天文字
     */
    public void receiveChatMessage(){
        String [] chatText=null;
        int i=0;
        while (true){
            try {
                chatText[i] = bufferedReader.readLine();
                if (chatText.equals(HttpAgreement.OVER_MESSAGE)) {
                    return;
                }
                i++;
            }catch (IOException e){
                errorLogArea.append(GetTime.getTime()+e.getMessage());
            }
        }
    }

    /**
     * 通过调用数据库持有者判断用户是否在线
     * @return
     */
    public void isOnline(){
        try {
            Boolean isonline = dataBaseUse.isOnline(bufferedReader.readLine());
            if(isonline)
                sendMessage(HttpAgreement.USER_ONLINE);
            else
                sendMessage(HttpAgreement.USER_NOTONLINE);
        }catch (IOException e){
            errorLogArea.append(GetTime.getTime()+e.getMessage());
            System.out.println("客户端托管这查询是否在线，读入id时错误");
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    /**
     * 根据用户输入的账号进行查询，返回目标的昵称,性别,年龄。
     */
    public void searchUsers_By_id(){
        List<String> list=null;
        try {
            list = dataBaseUse.searchUser(bufferedReader.readLine());             //读取用户端发来的id作为方法的参数
            if(list.size()==0) {
                sendMessage(HttpAgreement.NOT_FOUND_USER_RESPONSE);
            }
            else {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    sendMessage(it.next());
                }
            }
        }catch (IOException e){
            System.out.println("服务器查询目标异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    public void searchUserName(){
        try {
            String userName = dataBaseUse.searchUser_name(bufferedReader.readLine());  //读取用户端发来的群聊名称作为方法的参数
            sendMessage(userName);
        }catch (IOException e){
            System.out.println("服务器查询目标用户异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
    }
    public void searchGroup()
    {
        try {
            String master_id=dataBaseUse.searchGroup(bufferedReader.readLine());  //读取用户端发来的群聊名称作为方法的参数
            sendMessage(master_id);
        }catch (IOException e){
            System.out.println("服务器查询目标群聊异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    /**
     * 通过调用数据库使用者的register方法，进行注册
     * @param password
     * @param name
     * @param sex
     * @param age
     */
    public void registerUser(String password,String name,String sex,String age){
        Boolean complete=null;
        String id;
        do {
            if(!complete){
                errorLogArea.append(GetTime.getTime()+"注册过程自动生成的id可能重复，重新注册");
                System.out.println("注册过程自动生成的id可能重复，重新注册");
            }
            id = String.valueOf(random.nextInt(899999999) + 100000000);
            complete = dataBaseUse.register(id, password, name, sex, age);
        }while (!complete);
            sendMessage(HttpAgreement.REGIST_SUCCESS);
        userLoadArea.append(GetTime.getTime() + "id : " + id + "昵称 : " + name + "注册成功");
        System.out.println("注册成功");
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }

    /**
     * 通过调用数据库使用者的deleteFriend方法，删除用户好友，用户id通过bufferedReader.readLine()方法获得
     */
    public void deleteFriend(){
        Boolean success=false;
        try {
            success = dataBaseUse.deleteFriend(bufferedReader.readLine());
        }
        catch (IOException e){
            System.out.println("删除好友出现异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        if(success) {
            System.out.println("删除好友成功");
            sendMessage(HttpAgreement.DELETE_FRIEND_SUCCESS);
        }
        else {
            System.out.println("删除好友失败");
            sendMessage(HttpAgreement.DELETE_FRIEND_FAIL);
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    public void deleteGroup(){
        Boolean success=false;
        try {
            success = dataBaseUse.deleteGroup(bufferedReader.readLine(),bufferedReader.readLine());
        }
        catch (IOException e){
            System.out.println("删除好友出现异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        if(success) {
            System.out.println("删除好友成功");
            sendMessage(HttpAgreement.DELETE_GROUP_SUCCESS);
        }
        else {
            System.out.println("删除好友失败");
            sendMessage(HttpAgreement.DELETE_GROUP_FAIL);
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    public void createGroup(){
        String groupName,master_id;
        Boolean success=false;
        try {
            groupName = bufferedReader.readLine();
            master_id = bufferedReader.readLine();
            success=dataBaseUse.createGroup(groupName,master_id);
            if(success){
                System.out.println("创建群聊成功");
                sendMessage(HttpAgreement.CREATE_GROUP_SUCCESS);
            }
            else {
                System.out.println("创建群聊失败");
                sendMessage(HttpAgreement.CREATE_GROUP_FAIL);
            }
        }catch (IOException e){
            System.out.println("读取群名或群主ID异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }
    public void sendFile(String user_id){
        Thread SendFileThread=new Thread(){
            public void run(){
                Map<String,String[]> map=new HashMap<>();
                FileJudger fileJudger=new FileJudger(user_id);
                List<String[]> fileInfoList=fileJudger.getFile();
                if(fileInfoList.size()==0){
                    System.out.println(user_id+"没有需要接收的文件");
                }
                else {
                    for (String[] fileInfor:fileInfoList) {
                        String ip=dataBaseUse.getUser_IP(fileInfor[2]);
                        map.put(ip,fileInfor);
                    }
                    Iterator<Map.Entry<String,String[]>> it=map.entrySet().iterator();
                    while (it.hasNext()){
                        Map.Entry<String,String[]> mapentry=it.next();
                        new FileSend(mapentry.getKey(),mapentry.getValue(),errorLogArea);
                    }

                }
            }
        };
    }
    /**
     * 释放资源的方法
     * 此方法会关闭服务器与相应客户端之间的Socket连接，并在ClientManger对象中移除本对象
     */
    public void clearResource(){
        receiveThread.interrupt();
        waitForClient=false;
        dataBaseUse.setOnlineState(user_id,ip,false);
        try {
            sendMessage(HttpAgreement.CONNECT_CLOSE);
            socket.close();
        }catch (IOException e){
            System.out.println("连接关闭出现异常，或者向客户端发送断开连接信息异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
        clientManger.clientList.remove(this);
        sendMessage(HttpAgreement.OVER_MESSAGE);
    }

    /**
     * 向客户端发送消息的方法
     * @param message 消息内容
     */
    public void sendMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("服务器对客户端发送信息异常");
            errorLogArea.append(GetTime.getTime()+e.getMessage());
        }
    }

}

