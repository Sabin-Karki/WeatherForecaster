import java.awt.Cursor;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class WeatherApp extends JFrame{
    WeatherApp(){
       super("Weather Forecaster");

       //set size of gui
       setSize(400,520);

       //set whether the gui can be resized or not
       setResizable(false);

       //load gui to center
       setLocationRelativeTo(null);

       //operation peformed when closed
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       setLayout(null);

      addGUIcomponenets();

    

    }
    public void addGUIcomponenets(){

        //textfield

        JTextField tf1=new JTextField(60);
        tf1.setBounds(10,11,290,28);
        tf1.setFont(new Font("Dialog",Font.PLAIN,23));
        add(tf1);

        //Search 
        JButton searchBar=new JButton(loadImage("Assets\\search.png"));
        
        //change cursor to hand cursor when hovering it

        searchBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.setBounds(310, 8, 50, 32);
        add(searchBar);
        
 
    }


    //used to create image in gui 
    private ImageIcon loadImage(String resourcePath)
    {
     try{

        //read image file from path given
        BufferedImage image=ImageIO.read(new File(resourcePath));

        //returns image icon so our gui can render

        return  new ImageIcon(image);

     }catch(IOException e){
      e.printStackTrace();
     }

     System.out.println("Could not find resource");
    return null;

    }
}