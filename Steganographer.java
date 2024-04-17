import java.io.File;
import  java.io.IOException;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
public class Steganographer {
    public static void writeToClipboard(String s, ClipboardOwner owner) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, owner);
    }
    public static void encode(String password , BufferedImage image, String outFilename){
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

            colTemp = Integer.toBinaryString(g);
            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }
            
            g = Integer.parseUnsignedInt(colTemp,2);

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

            colTemp = Integer.toBinaryString(g);

            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }
            
            g = Integer.parseUnsignedInt(colTemp,2);

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

            colTemp = Integer.toBinaryString(g);

            if(counter > 0){
                colTemp = colTemp.substring(0,7) + "0";
                counter--;
                diff++;
            }
            else{
                colTemp = colTemp.substring(0,7) + temp.charAt(w-diff);
            }

            g = Integer.parseUnsignedInt(colTemp,2);

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

                colTemp = Integer.toBinaryString(g);
                while(colTemp.length() < 8){
                    colTemp="0"+colTemp;
                }

                colTemp = colTemp.substring(0,7) + temp.charAt(offsety);
                g = Integer.parseUnsignedInt(colTemp,2);

                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(startX+offsetx,startY+offsety,p);
            }
        }
        if(outFilename==null){
            outFilename="newKey.png";
        }
        try { // WRITE IMAGE 
            // Output file path 
            File output_file = new File(outFilename); 
  
            // Writing to file taking type and path as 
            ImageIO.write(image, "png", output_file); 
  
            System.out.println("Writing complete."); 
        } 
        catch (IOException e) { 
            System.out.println("Error: " + e); 
        } 
    }

    public static void decode(BufferedImage img){
        int xStart;
        int yStart;
        int passLength;
        String temp="";
        String colTemp=null;
        
        for(int i = 0; i < 7; i++){//get message length
            int p = img.getRGB(0,i);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            colTemp = Integer.toBinaryString(g);
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        passLength=Integer.parseUnsignedInt(temp,2);
        byte[] byteParse = new byte[passLength];
        temp="";
        colTemp = null;
        for(int i = 0; i < 12; i++){//get start x
            int p = img.getRGB(1,i);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            colTemp = Integer.toBinaryString(g);
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        xStart = Integer.parseUnsignedInt(temp,2);
        
        temp="";
        colTemp = null;

        for(int i = 0; i < 12; i++){//get start y
            int p = img.getRGB(2,i);
            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            colTemp = Integer.toBinaryString(g);
            temp = temp+colTemp.charAt(colTemp.length()-1);
        }
        yStart = Integer.parseUnsignedInt(temp,2);
        
        temp="";
        colTemp = null;

        for(int x = xStart; x < passLength+xStart; x++){
            for(int y = yStart; y < yStart+7; y++){
                int p = img.getRGB(x,y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                colTemp = Integer.toBinaryString(g);
                temp = temp+colTemp.charAt(colTemp.length()-1);
            }
            byteParse[x-xStart] = Byte.parseByte(temp,2);
            temp="";
        }
        String password = new String(byteParse);
        writeToClipboard(password,null);   
    }
    public static void main(String[] args) {
        String pass = "Mr.Kraft.Example";
        String filename = "";
        if(args.length > 0){
            filename = args[0];
        }
        else{
            filename="Key.png";
        }
        
        JFrame jf = new JFrame("Steganographer");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel btnPanel = new JPanel();
        JButton bl = new JButton("Encode Blue");
        JButton re = new JButton("Encode Red");
        JButton gr = new JButton("Encode Green");

        btnPanel.add(Box.createVerticalGlue());
        btnPanel.add(bl);
        btnPanel.add(Box.createVerticalGlue());
        btnPanel.add(re);
        btnPanel.add(Box.createVerticalGlue());
        btnPanel.add(gr);
        btnPanel.add(Box.createVerticalGlue());

        BufferedImage image = null; 
        // READ IMAGE 
        try { 
            File input_file = new File(filename); 
   
  
            // Reading input file 
            image = ImageIO.read(input_file); 
  
            System.out.println("Reading complete."); 
        } 
        catch (IOException e) { 
            System.out.println("Error: " + e); 
        } 
        if(args.length>1){
           pass=args[1];
           if(args.length>=2){
            encode(pass,image,args[2]);
           }
           else{
            encode(pass,image,filename); 
           }
           
        }
        if(args.length==1){
            decode(image);
        }
        
        
    }
    
}

