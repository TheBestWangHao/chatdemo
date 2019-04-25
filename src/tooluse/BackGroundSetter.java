package tooluse;

import javax.swing.*;
import java.awt.*;

public class BackGroundSetter {
    String picturePath;
    JFrame jFrame;
    public BackGroundSetter(String picturePath,JFrame jFrame) {
        this.picturePath=picturePath;
        this.jFrame=jFrame;
    }
    public void setBackGroundPicture(int x,int y,int length,int height){
        //((JPanel)loginPanel).setOpaque(false);
        JLabel backgroundLabel = new JLabel("");
        ImageIcon background = new ImageIcon(picturePath);
        Image image=background.getImage();
        image=image.getScaledInstance(length,height,Image.SCALE_AREA_AVERAGING);
        background=new ImageIcon(image);
        backgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
        backgroundLabel.setIcon(background);
        jFrame.getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));
    }

}
