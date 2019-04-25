package ChatClient;

import tooluse.BackGroundSetter;
import tooluse.CustomizeJLabel;
import tooluse.CustomizeJTextField;

import javax.swing.*;
import java.awt.*;

public class TestInterface extends JFrame {
    JPanel contentPanel;
    JScrollPane jScrollPane;
    JPanel headPanel;
    JPanel bottomPanel;
    Image image;
    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_HEIGHT = 900;

    public TestInterface() {
        setBounds(1000,0,PANEL_WIDTH,PANEL_HEIGHT);
        add(headPanel=new HeadPanel().getJPanel());
        headPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT / 6);

        setVisible(true);
        headPanel.setVisible(true);
        validate();

    }

   /* public void setHeadPanel() {
        headPanel.setLayout(null);
        headPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT / 6);
        CustomizeJLabel customizeJLabel = new CustomizeJLabel();
        JLabel jLabel = customizeJLabel.getJLabel(200, 30, 80, 30, "blank");
        CustomizeJTextField getJtextField = new CustomizeJTextField();
        JTextField jtf = getJtextField.getJTextField(200, 70, 80, 30);
        headPanel.add(jLabel);
        headPanel.add(jtf);
        // paintImage();
    }*/
   public void setHeadPanel(){

   }
    private class HeadPanel extends JPanel{
        Image image;
        JLabel jLabel;
        JTextField jtf;

        public HeadPanel(){
            setLayout(null);
            paintImage();
            setJLabel();
            setJTextField();
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
