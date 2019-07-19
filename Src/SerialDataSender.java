/*
 * @projectTitle FaceLock - face recognition and tracking 
 * @authors Shane Vance, Bardia Borhani, and Alex Puga
 *
 */

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

public class SerialDataSender {

  private boolean isArduinoError = false;
  private ByteArrayOutputStream bOut = new ByteArrayOutputStream();
  private OutputStreamWriter outputWriter = new OutputStreamWriter(this.bOut); private static final String PORT = "COM3"; public SerialDataSender() {
   
  CommPortIdentifier portId = findPortId();

    try {
      this.serialPort = (SerialPort)portId.open(getClass().getName(), 2000);
      this.serialPort.setSerialPortParams(9600, 8, 1, 0);
      this.output = this.serialPort.getOutputStream();
      this.serialPort.notifyOnDataAvailable(true);
    }
    catch (Exception e) {
      
      e.getStackTrace();
      this.isArduinoError = true;
    } 
  }
  private static final int TIME_OUT = 2000; private static final int DATA_RATE = 9600;
  private SerialPort serialPort;
  private OutputStream output;
  String sData;
  
  public boolean isArduinoError() { return this.isArduinoError; }
  
  public void SendDataToArduino(int x, int y) {
    String sY;
    String sX;
    if (x > 99) {
      
      sX = Integer.toString(x);
    }
    else if (x > 9) {
      
      sX = "0" + Integer.toString(x);
    }
    else {
      
      sX = "00" + Integer.toString(x);
    } 
    
    if (y > 99) {
      
      sY = Integer.toString(y);
    }
    else if (y > 9) {
      
      sY = "0" + Integer.toString(y);
    }
    else {
      
      sY = "00" + Integer.toString(y);
    } 
    
    try {
      this.sData = String.valueOf(sX) + "-" + sY + "@";
      this.outputWriter.write(this.sData);
      this.outputWriter.flush();
      this.bOut.writeTo(this.output);
      this.bOut.reset();
    }
    catch (Exception e) {
      
      e.printStackTrace();
    } 
  }
  
  private CommPortIdentifier findPortId() {
    CommPortIdentifier portId = null;
    Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
    
    while (portEnum.hasMoreElements()) {
      
      CommPortIdentifier currPortId = (CommPortIdentifier)portEnum.nextElement();
      
      if (currPortId.getName().equals("COM3")) {
        
        portId = currPortId;

        
        break;
      } 
    } 
    
    return portId;
  }
}
