import java.io.File;
import  java.io.IOException;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
public class Steganographer implements ActionListener{
    JFileChooser jfc = new JFileChooser();
    static JTextField jt;
    BufferedImage image;
    JButton bl,gr,re,bld,grd,red,open,save;
    JFrame jf = new JFrame("Steganographer");
    File file = null;
    public static void writeToClipboard(String s, ClipboardOwner owner) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, owner);
    }
    public static void encode(String password,char col,BufferedImage image){
        String colTemp=null;
        String temp;
        byte[] charbyte = password.getBytes();   
        int width= image.getWidth();
        int height = image.getHeight();

        final int startX = (int)(Math.random()*(width-password.length()))+1;
        final int startY = (int)(Math.random()*(height-password.length()-8))+3;
        temp = Integer.toBinaryString(password.length());
        int counter = 7-temp.length();
        int diff = 0;
        for(int w = 0; w < 7; w++){//encode message length
            int p = image.getRGB(0,w);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }
            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }
            if(col == 'b'){
                b = Integer.parseUnsignedInt(colTemp,2); 
            }
            if(col == 'r'){
                r = Integer.parseUnsignedInt(colTemp,2);
            }
            if(col == 'g'){
                g = Integer.parseUnsignedInt(colTemp,2);
            }
                
            

            p = (a << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(0,w,p);
        }
        temp = Integer.toBinaryString(startX);
        counter = 12-temp.length();
        diff = 0;
        for(int w = 0; w < 12; w++){//encode start x
            int p = image.getRGB(1,w);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }

            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }
            
            if(col == 'b'){
                b = Integer.parseUnsignedInt(colTemp,2); 
            }
            if(col == 'r'){
                r = Integer.parseUnsignedInt(colTemp,2);
            }
            if(col == 'g'){
                g = Integer.parseUnsignedInt(colTemp,2);
            }

            p = (a << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(1,w,p);
        }
        temp = Integer.toBinaryString(startY);
        counter = 12-temp.length();
        diff = 0;
        for(int w = 0; w < 12; w++){//encode start y
            int p = image.getRGB(2,w);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }

            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }

            if(col == 'b'){
                b = Integer.parseUnsignedInt(colTemp,2); 
            }
            if(col == 'r'){
                r = Integer.parseUnsignedInt(colTemp,2);
            }
            if(col == 'g'){
                g = Integer.parseUnsignedInt(colTemp,2);
            }

            p = (a << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(2,w,p);
        }
        
        for(int offsetx = 0; offsetx < password.length(); offsetx++){
            temp = Integer.toBinaryString(charbyte[offsetx]);
            while(temp.length() < 7){
                temp="0"+temp;
            }
            for(int offsety = 0; offsety < 7; offsety++){
                int p = image.getRGB(startX+offsetx,startY+offsety);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                if(col == 'b'){
                    colTemp = Integer.toBinaryString(b); 
                }
                if(col == 'r'){
                    colTemp = Integer.toBinaryString(r); 
                }
                if(col == 'g'){
                    colTemp = Integer.toBinaryString(g); 
                }
                while(colTemp.length() < 8){
                    colTemp="0"+colTemp;
                }

                colTemp = colTemp.substring(0,7) + temp.charAt(offsety);
                if(col == 'b'){
                    b = Integer.parseUnsignedInt(colTemp,2); 
                }
                if(col == 'r'){
                    r = Integer.parseUnsignedInt(colTemp,2);
                }
                if(col == 'g'){
                    g = Integer.parseUnsignedInt(colTemp,2);
                }

                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(startX+offsetx,startY+offsety,p);
            }
        }
        jt.setText("Message Encoded");
    }

    public static void decode(BufferedImage img,char col){
        int xStart;
        int yStart;
        int passLength;
        String temp="";
        String colTemp=null;
        
        for(int i = 0; i < 7; i++){//get message length
            int p = img.getRGB(0,i);
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        passLength=Integer.parseUnsignedInt(temp,2);
        byte[] byteParse = new byte[passLength];
        temp="";
        colTemp = null;
        for(int i = 0; i < 12; i++){//get start x
            int p = img.getRGB(1,i);
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        xStart = Integer.parseUnsignedInt(temp,2);
        
        temp="";
        colTemp = null;

        for(int i = 0; i < 12; i++){//get start y
            int p = img.getRGB(2,i);
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            if(col == 'b'){
                colTemp = Integer.toBinaryString(b); 
            }
            if(col == 'r'){
                colTemp = Integer.toBinaryString(r); 
            }
            if(col == 'g'){
                colTemp = Integer.toBinaryString(g); 
            }
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        yStart = Integer.parseUnsignedInt(temp,2);
        
        temp="";
        colTemp = null;

        for(int x = xStart; x < passLength+xStart; x++){
            for(int y = yStart; y < yStart+7; y++){
                int p = img.getRGB(x,y);
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                if(col == 'b'){
                    colTemp = Integer.toBinaryString(b); 
                }
                if(col == 'r'){
                    colTemp = Integer.toBinaryString(r); 
                }
                if(col == 'g'){
                    colTemp = Integer.toBinaryString(g); 
                }
                temp = temp+colTemp.charAt(colTemp.length()-1);
            }
            byteParse[x-xStart] = Byte.parseByte(temp,2);
            temp="";
        }
        String password = new String(byteParse);
        writeToClipboard(password,null);   
    }
    

    public Steganographer(){
        
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel,BoxLayout.X_AXIS));
        JPanel decPanel = new JPanel();
        decPanel.setLayout(new BoxLayout(decPanel, BoxLayout.X_AXIS));
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        jt = new JTextField();
        bl = new JButton("Encode Blue");
        re = new JButton("Encode Red");
        gr = new JButton("Encode Green");
        bld = new JButton("Decode Blue");
        red = new JButton("Decode Red");
        grd = new JButton("Decode Green");
        open = new JButton("Open File");
        save = new JButton("Save File");

        bl.addActionListener(this);
        re.addActionListener(this);
        gr.addActionListener(this);
        bld.addActionListener(this);
        red.addActionListener(this);
        grd.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);

        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(bl);
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(re);
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(gr);
        btnPanel.add(Box.createHorizontalGlue());

        decPanel.add(Box.createHorizontalGlue());
        decPanel.add(bld);
        decPanel.add(Box.createHorizontalGlue());
        decPanel.add(red);
        decPanel.add(Box.createHorizontalGlue());
        decPanel.add(grd);
        decPanel.add(Box.createHorizontalGlue());

        filePanel.add(Box.createHorizontalGlue());
        filePanel.add(open);
        filePanel.add(Box.createHorizontalGlue());
        filePanel.add(save);
        filePanel.add(Box.createHorizontalGlue());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));

        content.add(btnPanel);
        content.add(Box.createVerticalGlue());
        content.add(decPanel);
        content.add(Box.createVerticalGlue());
        content.add(filePanel);
        content.add(Box.createVerticalGlue());
        content.add(jt);
        jt.setText("Write message here");
        
        jf.add(content);
        jf.pack();
        jf.setVisible(true);
    }
    public static void main(String[] args) {
       
        javax.swing.SwingUtilities.invokeLater(
         () -> new Steganographer()
      );
       
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "Encode Blue"){
            encode(jt.getText(),'b',image);
        }else if(e.getActionCommand() == "Encode Red"){
            encode(jt.getText(),'r',image);
        }else if(e.getActionCommand() == "Encode Green"){
            encode(jt.getText(),'g',image);
        }else if(e.getActionCommand() == "Decode Blue"){
            decode(image,'b');
        }else if(e.getActionCommand() == "Decode Red"){
            decode(image,'r');
        }else if(e.getActionCommand() == "Decode Green"){
            decode(image,'g');
        }
        
        
        if(e.getActionCommand()=="Open File"){
            int returnVal = jfc.showOpenDialog(jf);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = jfc.getSelectedFile();
                try{
                image = ImageIO.read(file);
                }catch(IOException x){
                    jt.setText("File failed to open" );
                }
            }
        }
        if(e.getActionCommand()=="Save File"){
            int returnVal = jfc.showSaveDialog(jf);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = jfc.getSelectedFile();
                try {           
                    // Writing to file taking type and path as 
                    ImageIO.write(image, "png", file); 
                } 
                catch (IOException x) { 
                   jt.setText("File failed to write"); 
                } 
            }
        }
    }
}

