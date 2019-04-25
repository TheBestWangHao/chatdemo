package Serve.service;

import tooluse.GetTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Statement;
import java.util.List;

/*此类为处理逻辑业务的类，其对下实例化客户端托管对象，向客户端传递上层图形化管理界面的指令，对上传递日志数据，其实际为中间平台类*/

/**
 * 类全程：客户端管理器
 */
public class ClientManger {
    public static final int REFUES_LOAD=404;
    private boolean waitForClient   =  true;                //是否等待客户端连接
    private boolean passLogDate     =  true;                //是否传输日志数据
    private boolean waitForReceiveFile;
    private List<FileReceive> fileSocketList;
    private JTextArea serveLoadArea;                       //Manger中显示服务器访问记录的JTextarea
    private JTextArea userLoadArea;                         //Manger中的显示用户登录日志的JTextarea
    private JTextArea errorLogArea;                         //Manger中的显示错误记录的日志
    private Statement statement;                            //与数据库直接连接的对象
    private Thread initThread;                              //初始化时的线程对象
    private Thread fileThread;
    private ServerSocket serverSocket;
    private ServerSocket fileServeSocket;
    private DefaultTableModel defaultTableModel;
    public  List<Clienter> clientList;
    private static int port=10000;
    private static int filePort=9000;
    private DataBaseUser dataBaseUser=new DataBaseUser();              //数据库操作类的对象

    public ClientManger(JTextArea serveLoadArea,JTextArea userLoadArea,JTextArea errorLogArea,DefaultTableModel defaultTableModel){
        this.serveLoadArea=serveLoadArea;
        this.userLoadArea=userLoadArea;
        this.errorLogArea=errorLogArea;
        this.defaultTableModel=defaultTableModel;
        System.out.println("客户端托管者管理员已创建");
       // statement=dataBaseUser.getConnection();
    }
    /**
     * 向图形化服务器管理界面发送 服务器访问记录日志
     * @param message 信息内容
     */
    public void sendServeLoadMessage(String message){
        serveLoadArea.append(GetTime.getTime()+message);
    }
    /**
     * 向图形化服务器管理界面发送 用户登录日志
     * @param message 信息内容
     */
    public void sendUserLoadMessage(String message){
        userLoadArea.append(GetTime.getTime()+message);
    }

    /**
     * 向图形化服务器管理界面发送 错误信息日志
     * @param message 信息内容
     */
    public void sendErrorMessage(String message){
        errorLogArea.append(GetTime.getTime()+message);
    }
    public void myInit(){
        ClientManger parent=this;
        initThread=new Thread(){
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println(port+"端口监听开始");
                }catch (IOException e){
                    System.out.println("服务器监听启动失败");
                    sendErrorMessage("服务器监听启动失败");
                }
                while (waitForClient){
                    try{
                         clientList.add(new Clienter(serverSocket.accept(),parent,dataBaseUser,statement,defaultTableModel) );
                        System.out.println(port + "新的服务器托管对象已建立");
                    }catch (IOException e) {
                        System.out.println("服务器接收连接失败");
                        sendErrorMessage("服务器监听启动失败");
                    }
                }
            }
        };
        initThread.start();
        fileThread=new Thread(){
            public void run(){
                while (waitForReceiveFile){
                    try {
                        fileServeSocket = new ServerSocket(filePort);
                        System.out.println(port+"端口监听开始");
                    }catch (IOException e){
                        System.out.println("文件监听启动失败");
                        sendErrorMessage("文件监听启动失败");
                    }
                    try{
                        fileSocketList.add(new FileReceive(fileServeSocket.accept(),errorLogArea));
                        System.out.println(port + "新的文件接收对象已建立");
                    }catch (IOException e) {
                        System.out.println("文件传输接收连接失败");
                        sendErrorMessage("文件传输监听启动失败");
                    }
                }
            }
        };
        fileThread.start();
    }

    /**
     * 清除所有已经创建的客户端托管对象，释放他们的资源。
     * 建议在关闭服务器时调用，或者关闭外部连接时调用
     */
    public void stopServe(){
        for (Clienter clienter:clientList) {
            clienter.sendMessage(HttpAgreement.CLOSED_SERVICE);
            clienter.clearResource();
            clientList.clear();
        }
    }
}
