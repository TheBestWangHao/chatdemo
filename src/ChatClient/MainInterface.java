package ChatClient;
import tooluse.BackGroundSetter;
import tooluse.CustomizeJLabel;
import tooluse.CustomizeJTextField;
import tooluse.GetTime;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class MainInterface extends JFrame {
    InnerPanel innerPanel;
    String user_id,user_name;
    JPanel innerjPanel;
    HeadPanelSetter headPanelSetter;
    JPanel contentPanel;
    JScrollPane jscrollPane;
    JPanel headPanel;
    JPanel bottomPanel;
    Image image;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    InputStream inputStream;
    OutputStream outputStream;
    ServerSocket serverSocket;
    Socket socket;
    Thread receiveMessageThread;
    public static final int CONNECT_PORT=10000;
    public static final String SERVE_IP="";
    private static final int PANEL_WIDTH=400;
    private static final int PANEL_HEIGHT=900;
    public MainInterface(String id){
        createSocket();
        user_id=id;
        setUserInfo();
        setBounds(1000,0,PANEL_WIDTH,PANEL_HEIGHT);
        setVisible(true);
        setHeadPanel();
        setJscrollPane();
        receiveMessage();
        validate();
    }
    public void setHeadPanel(){
        headPanelSetter=new HeadPanelSetter();
        add(headPanel=headPanelSetter.getJPanel());
        headPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT / 6);
        headPanel.setVisible(true);
    }
    public void setJscrollPane(){
        innerPanel=new InnerPanel();
        innerjPanel=innerPanel.getJPane();
        jscrollPane=new JScrollPane(innerjPanel);
        jscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(jscrollPane);
        jscrollPane.setBounds(0,PANEL_HEIGHT / 6,PANEL_WIDTH,PANEL_HEIGHT / 2);
        jscrollPane.setVisible(true);
    }
    public void receiveMessage(){
        receiveMessageThread=new Thread(){
            String message=null;
            public void run(){
                while (true) {
                    try {
                        message = bufferedReader.readLine();
                    }catch (IOException e){
                        System.out.println("从服务器接收消息失败");
                        e.printStackTrace();
                    }
                        if (message.equals(HttpAgreement.FRIEND_LIST_ADD_REQUEST))
                            innerPanel.receiveFriendList();
                        if (message.equals(HttpAgreement.FRIEND_LIST_delete_REQUEST))
                            innerPanel.deleteFriendLabel();
                }
            }
        };
        receiveMessageThread.start();
    }
    public void createSocket(){
        try {
            socket = new Socket(SERVE_IP, CONNECT_PORT);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
        }catch (IOException e){
            System.out.println("客户端与服务器建立连接失败");
            e.printStackTrace();
        }

    }
    public void setUserInfo(){
        sendMessage(HttpAgreement.SEARCH_USER_NAME_REQUEST);
        try {
            user_name=bufferedReader.readLine();
        }catch (IOException e){
            System.out.println("获取登陆者用户名时出现异常");
        }
    }
    public void sendMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("客户端对服务器发送请求异常");
            e.printStackTrace();
        }
    }
    private class InnerPanel extends JPanel{
        Map<String,String> friendMap=new HashMap<>();
        Map<String,JLabel> labelMap=new HashMap<>();
        GridLayout gridLayout;
        public InnerPanel(){
            setPanelLayout();
            receiveFriendList();
            setVisible(true);
        }
        public void setPanelLayout(){
            gridLayout=new GridLayout();
            gridLayout.setColumns(1);
            gridLayout.setRows(1);
            gridLayout.setHgap(0);
            setLayout(gridLayout);
        }
        public void changeGridLayout(){
            gridLayout.setColumns(labelMap.size());
            validate();
        }
        public void receiveFriendList() {
            while (true) {
                try {
                    String key = bufferedReader.readLine();
                    if (key.equals(HttpAgreement.OVER_MESSAGE))
                        break;
                    String value = bufferedReader.readLine();
                    friendMap.put(key, value);
                    createFriendLabel(key, value);
                } catch (IOException e) {
                    System.out.println("客户端获取好友列表错误");
                    e.printStackTrace();
                }
            }
        }
        public void deleteFriendLabel(){
            while (true){
                try {
                    String id = bufferedReader.readLine();
                    if (id.equals(HttpAgreement.OVER_MESSAGE))
                        break;
                    friendMap.remove(id);
                    remove(labelMap.get(id));
                    labelMap.remove(id);
                    changeGridLayout();
                    repaint();
                }catch (IOException e){
                    System.out.println("客户端删除好友列表中好友失败");
                    e.printStackTrace();
                }
            }
        }
        public void createFriendLabel(String id,String name){
            Toolkit toolkit=Toolkit.getDefaultToolkit();
            Image image=toolkit.getImage("D:\\12.png");
            image.getScaledInstance(100,100,Image.SCALE_DEFAULT);
            ImageIcon imageIcon=new ImageIcon(image);
            JLabel jLabel=new JLabel("<html>"+id+"<br />"+name+"</html>",imageIcon,JLabel.RIGHT);
            labelMap.put(id,jLabel);
            changeGridLayout();
        }
        public JPanel getJPane(){
            return this;
        }
    }
    private class HeadPanelSetter extends JPanel{
        Image image;
        JLabel jLabel;
        JTextField jtf;

        public HeadPanelSetter(){
            setLayout(null);
            setJLabel();
            setJTextField();
            paintImage();
            this.setVisible(true);
        }
        public void setJLabel(){
            CustomizeJLabel customizeJLabel=new CustomizeJLabel();
            jLabel=customizeJLabel.getJLabel(200,30,80,30,"blank");
            this.add(jLabel);
        }
        public void setJTextField(){
            CustomizeJTextField getJtextField=new CustomizeJTextField();
            jtf=getJtextField.getJTextField(200,70,80,30);
            this.add(jtf);
        }
        public void paintImage(){
            Toolkit toolkit=getToolkit();
            image =toolkit.getImage("D:\\1.jpg");
        }
        public void paint(Graphics g){
            super.paint(g);
            g.drawImage(image,20,30,60,60,null);
        }
        public JPanel getJPanel(){
            return this;
        }
    }
}
