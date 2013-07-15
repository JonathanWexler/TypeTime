package game;

import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.DataOutputStream;  
import java.io.DataInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
  
public class StopWatch {  
    private long startTime;  
    private long endTime;  
    private double currentTime;  
    private double fastestTime;  
    private DataOutputStream output;  
    private DataInputStream input;  
      
    public StopWatch() {  
        readFastestTime();  
          
    } //end constructor  
      
    public void startTimer() {  
        startTime = System.currentTimeMillis();  
    }  
      
    public void stopTimer() {  
        endTime = System.currentTimeMillis();  
        calculateTime();  
          
    }  
      
    private void calculateTime() {  
        long howLongInMillis = endTime - startTime;  
        currentTime = (double) howLongInMillis / 1000;  
          
    } // end method calculateTimeInSchool  
       
    public double getTime() {  
        return currentTime;  
    }  
      
    public double getFastestTime() {  
        return fastestTime;  
    }  
      
    public void readFastestTime() {  
          
        try {  
            input = new DataInputStream( new FileInputStream("FastestTime.ser") );  
            fastestTime = input.readDouble();  
            input.close();  
        } catch (FileNotFoundException ex) {  
            System.err.println("Tried to read file but file not found");  
            // can't find file then create one!  
            writeFastestTime();  
        } catch (IOException ex) {  
            System.err.println("Tired to read file but IOException");  
        }  
          
    } // end method readFastestTime  
      
    public void writeFastestTime() {  
          
        try {  
            output = new DataOutputStream( new FileOutputStream("FastestTime.ser") );  
            output.writeDouble(fastestTime);  
            output.close();  
          
        } catch (FileNotFoundException ex) {  
            System.err.println("File not found");  
              
        } catch (IOException ex) {  
            System.err.println("IOException");  
        }  
          
    } // end method writeFastestTime  
      
    public boolean isFaster() {  
          
        if ( (currentTime < fastestTime) || (fastestTime == 0.0)){  
            fastestTime = currentTime;  
            writeFastestTime();  
            return true;  
        } else {  
            return false;  
        }  
    } // end method isFaster  
      
    public void resetFastestTime() {  
        fastestTime = 0.0;  
        writeFastestTime();  
    }  
      
} // end class StopWatch  