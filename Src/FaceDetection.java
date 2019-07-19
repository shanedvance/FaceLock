/*
 * @projectTitle FaceLock - face recognition and tracking 
 * @authors Shane Vance, Bardia Borhani, and Alex Puga
 *
 */

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection
{
  protected static boolean blackwhite;
  private int amountOfFaces;
  private boolean isFace;
  private boolean isCascadeError;
  private CascadeClassifier fcc;
  protected CommPortIdentifier cpi;
  protected CommPort cp;
  protected SerialPort sp;
  protected SerialDataSender sds;
  private int preX;
  private int currX;
  private int preY;
  private int currY;
  
  public FaceDetection() { this("../FaceLock-r2/cascades/haarcascade_frontalface_alt.xml"); }
  public FaceDetection(String cascade_classifier) {
    this.preX = 0;
    this.currX = 0;
    this.preY = 0;
    this.currY = 0;
    this.amountOfFaces = 0;
    this.isFace = false;
    this.isCascadeError = false;   
    this.sds = new SerialDataSender();

    getFaceCascade(cascade_classifier);
  }
  
  private void getFaceCascade(String str) {
    this.fcc = new CascadeClassifier(str);
    
    if (this.fcc.empty())
    {
      this.isCascadeError = true;
    }
  }
  
  public Mat detectFace(Mat iframe) {
    Mat rb = iframe;
    Mat lb = iframe;
    
    MatOfRect mor = new MatOfRect();
    
    if (blackwhite) {
      
      Imgproc.line(rb, new Point((iframe.width() / 2), iframe.height()), new Point((iframe.width() / 2), 0.0D), new Scalar(12.0D, 12.0D, 12.0D), 2);
      Imgproc.line(rb, new Point(0.0D, (iframe.height() / 2)), new Point(iframe.width(), (iframe.height() / 2)), new Scalar(12.0D, 12.0D, 12.0D), 2);
      Imgproc.cvtColor(rb, lb, 6);
      Imgproc.equalizeHist(lb, lb);
    } 
    
    this.fcc.detectMultiScale(lb, mor);
    
    this.amountOfFaces = mor.toArray().length;
    
    if (this.amountOfFaces > 0){
      this.isFace = true;
    }

    
    if (this.amountOfFaces == 0) {
      
      Imgproc.putText(rb, "Face Count = 0", new Point((iframe.width() - 150), (iframe.height() - iframe.height() - 20)), 
          1, 1.0D, new Scalar(255.0D, 255.0D, 255.0D));
    }
    else {
      
      this.preX = this.currX;
      this.preY = this.currY; byte b; int i;
      Rect[] arrayOfRect;
      for (i = arrayOfRect = mor.toArray().length, b = 0; b < i; ) { Rect rec = arrayOfRect[b];
        
        this.currX += rec.x + rec.width / 2;
        this.currX /= this.amountOfFaces;
        
        this.currY += rec.y + rec.height / 2;
        this.currY /= this.amountOfFaces;
        
        Imgproc.rectangle(rb, new Point(rec.x, rec.y), new Point((rec.x + rec.width), (rec.y + rec.height)), new Scalar(12.0D, 255.0D, 0.0D), 2);
        
        Imgproc.putText(rb, "Face Count = " + Integer.toString(this.amountOfFaces), new Point((iframe.width() - 150), (iframe.height() - iframe.height() - 20)), 
            1, 1.0D, new Scalar(255.0D, 255.0D, 255.0D));
        
        b++; }
    
    } 
    
    arduinoController();
    
    return rb;
  }
  
  private void arduinoController() {
    if (this.preX < this.currX - 10 || this.preX > this.currX + 10 || this.preY < this.currY - 10 || this.preY > this.currY + 10) {
      
      this.sds.SendDataToArduino(this.currX, this.currY);
      this.preX = this.currX;
      this.preY = this.currY;
    } 
  }
  
  public String amountOfFaces() { return Integer.toString(this.amountOfFaces); }
 
  public boolean isFace() { return this.isFace; }
  
  public boolean isSerialError() { return this.sds.isArduinoError(); }
 
  public boolean isCascadeError() { return this.isCascadeError; }
}
