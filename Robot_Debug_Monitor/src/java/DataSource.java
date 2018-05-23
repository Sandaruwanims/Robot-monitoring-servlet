

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */


public class DataSource {
    
    private String message = null;
    
    
    final synchronized String readReadings(String serialPrint) throws InterruptedException {
        
        message = serialPrint;
        notifyAll();
        return "Success";
        
    }
    
    
    final synchronized String getReadings() throws InterruptedException {
        
        return message;
    }
    
}
