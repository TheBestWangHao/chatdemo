package Serve.allsurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Manger extends JFrame {
    CardLayout cardLayout;
    JPanel cardLayoutPanel,panel;
    Font font;
    JButton userButton,servicButton;
    private static int MANGER_WIDTH=1000,MANGER_HEIGHT=800;
    private static int PANEL_WIDTH=1000,PANEL_HEIGHT=600;
    private static int CARDPANEL_WIDTH=1000,CARDPANEL_HEIGHT=770;
    public Manger(){
        this.setBounds(450,150,MANGER_WIDTH,MANGER_HEIGHT);
        setTitle("后台管理界面");
        font=new Font("楷体",Font.PLAIN,25);
        panel=new JPanel();
        cardLayoutPanel=new JPanel();
        cardLayoutPanel.setBounds(0,50,1000,600);
        setCardLayoutPanel();
        setPanel();
        panel.add(cardLayoutPanel);
        this.setContentPane(panel);
        this.setVisible(true);
        addButtonAction();
    }
    private void setPanel(){
        panel.setLayout(null);
        panel.setBounds(0,0,PANEL_WIDTH,PANEL_HEIGHT);
        userButton=getButton(0,0,505,30,"用           户");
        servicButton=getButton(500,0,500,30,"服     务     器");
        panel.add(userButton);
        panel.add(servicButton);
    }
    private  void setCardLayoutPanel() {
        cardLayout=new CardLayout();
        cardLayoutPanel.setLayout(cardLayout);
        cardLayoutPanel.setBounds(0,30,1000,770);
        cardLayoutPanel.add("user",new JTableSurface().getJpanel());
        cardLayoutPanel.add("service",new ServiceSurface().getJpanel());
    }
    private JButton getButton(int x,int y,int width,int height,String text) {
        JButton but=new JButton(text);
        but.setFont(font);
        but.setBounds(x,y,width,height);
        return but;
    }
    private void addButtonAction() {
        userButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(cardLayoutPanel.getLayout()!=new CardLayout())
                System.out.println("wrong");
                else
                System.out.println("NOT WRONG");
                cardLayout.show(cardLayoutPanel,"user");
            }
        });
        servicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardLayoutPanel,"service");
            }
        });
    }
}
