/*
 * @projectTitle FaceLock - face recognition and tracking 
 * @authors Shane Vance, Bardia Borhani, and Alex Puga
 *
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Client
{
  public static void main(String[] args) throws InterruptedException {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    
    FaceDetection faceDetection = new FaceDetection();
    Mat wi = new Mat();
    VideoCapture videoCapture = new VideoCapture(1);
    
    if (videoCapture.isOpened() && !faceDetection.isSerialError() && !faceDetection.isCascadeError()) {
      
      FacePanel facePanel = new FacePanel("FaceLock r2 - Demo");
      facePanel.add(facePanel);
      facePanel.behindTheScenes();
      Thread.sleep(500L);

      
      while (true) {
        videoCapture.read(wi);
        
        if (!wi.empty()) {
          
          facePanel.setSize(wi.width() + 28, wi.height() + 58);
          wi = faceDetection.detectFace(wi);
          facePanel.matToBufferedImage(wi);
          facePanel.repaint();


          
          continue;
        } 

        
        break;
      } 
    } else if (faceDetection.isSerialError()) {
      
      JOptionPane.showMessageDialog(new JFrame(), "The following error(s) are occurring with FaceLock:\n\n1.) Arduino is not connected\nor\n2.) Arduino is not set to COM3", 
          "FaceLock - Error Message", 0);
    }
    else if (faceDetection.isCascadeError()) {
      
      JOptionPane.showMessageDialog(new JFrame(), "There is an error with loading a file for this program.\nPlease contact team FaceLock for help.", 
          "FaceLock - Error Message", 0);
    }
    else {
      
      JOptionPane.showMessageDialog(new JFrame(), "The WebCam is not connected.\nPlease connect device or close program.", 
          "FaceLock - Error Message", 0);
    } 

    
    videoCapture.release();
  }
}