package Serve.allsurface;

import Serve.service.ClientManger;
import Serve.service.Clienter;
import tooluse.CustomizeJButton;
import tooluse.CustomizeJLabel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServiceSurface {
    JPanel jPanel;
    JButton startServiceButton,closeServiceButton;
    JLabel portLabel;
    JTextField portTextField;
    CustomizeJButton customizeJButton;
    CustomizeJLabel customizeJLabel;
    ClientManger clientManger;
    JTextArea serveLoadArea,userLoadArea,errorLogArea;
    public ServiceSurface(){
        jPanel=new JPanel();
        jPanel.setLayout(null);
        customizeJButton=new CustomizeJButton();
        customizeJLabel=new CustomizeJLabel();
        serveLoadArea=new JTextArea();
        userLoadArea=new JTextArea();
        errorLogArea=new JTextArea();
        setJpanel();
    }
    public void setJpanel(){
        startServiceButton=customizeJButton.getButton(30,30,120,50,"打开服务器");
        closeServiceButton=customizeJButton.getButton(130,30,120,50,"关闭服务器");
        portLabel=customizeJLabel.getJLabel(30,130,120,40,"服务器使用端口: ");
        jPanel.add(startServiceButton);
        jPanel.add(closeServiceButton);
        jPanel.add(portLabel);
        addButtonAction();

    }
    public JPanel getJpanel(){
        return jPanel;
    }
    public void addButtonAction(){
        startServiceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clientManger=new ClientManger(serveLoadArea,errorLogArea,userLoadArea,JTableSurface.dm);
                clientManger.myInit();
            }
        });
    }
}
