/*
 * @projectTitle FaceLock - face recognition and tracking 
 * @authors Shane Vance, Bardia Borhani, and Alex Puga
 *
 */

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class FacePanel extends JPanel {

  public FacePanel() {
    this.mainFrame = new JFrame("Face Panel");
    this.mainFrame.setVisible(true);
    this.mainFrame.setResizable(false);
    this.mainFrame.setSize(800, 800);
    this.mainFrame.setDefaultCloseOperation(3);
  }

  private static final long serialVersionUID = 1L;
  private BufferedImage img;
  private JFrame mainFrame;
  
  public FacePanel(String title) {
    this.mainFrame = new JFrame(title);
    this.mainFrame.setVisible(true);
    this.mainFrame.setResizable(false);
    this.mainFrame.setDefaultCloseOperation(3);
  }
  
  public FacePanel(String title, int width, int height) {
    this.mainFrame = new JFrame(title);
    this.mainFrame.setVisible(true);
    this.mainFrame.setResizable(false);
    this.mainFrame.setSize(width, height);
    this.mainFrame.setDefaultCloseOperation(3);
  }
 
  public void add(FacePanel facePanel) { this.mainFrame.add(facePanel); }
  
  public void behindTheScenes() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(1));
    JButton button1 = new JButton("Behind the Scene's");
    JButton button2 = new JButton("Normal");
    button1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            FaceDetection.blackwhite = true;
          }
        });
    button2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            FaceDetection.blackwhite = false;
          }
        });
    panel.add(button1);
    panel.add(button2);
    this.mainFrame.add(panel, "South");
  }
 
  public void setSize(int width, int height) { this.mainFrame.setSize(width, height); }

  
  public boolean matToBufferedImage(Mat matrix) {
    MatOfByte mb = new MatOfByte();
    Imgcodecs.imencode(".jpg", matrix, mb);
   
    try {
      this.img = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
    }
    catch (IOException e) {      
      e.printStackTrace();
      return false;
    } 

    
    return true;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    if (this.img == null) {
      return;
    }
    
    g.drawImage(this.img, 10, 10, this.img.getWidth(), this.img.getHeight(), null);
  }
}
