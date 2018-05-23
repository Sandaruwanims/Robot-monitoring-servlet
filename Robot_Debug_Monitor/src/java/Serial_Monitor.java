/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(urlPatterns = {"/RobotSerialData"})

public class Serial_Monitor extends HttpServlet {
    
    private final DataSource source = new DataSource();
    
    @Override
    public void init() {
        ServletConfig config = getServletConfig();
        //get the value of the init-parameter
        ServletContext sc = config.getServletContext();
        Logger.getGlobal().log(Level.INFO, "Serial Monitor started");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        response.setContentType("text/event-stream,charset=UTF-8");
        
        try (final PrintWriter out = response.getWriter()) {
            
            while (!Thread.interrupted()) {
                
                synchronized (source) {
                    source.wait();
                    out.print("data: ");
                    out.println(source.getReadings());
                    out.println();
                    out.flush();
                }
                
            }
            
        } catch (InterruptedException ex) {
//            Logger.getLogger(Serial_Monitor.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String message = "faild";
        
        try {
            
            message = source.readReadings(request.getParameter("message"));
            
        } catch (InterruptedException ex) {
            
//            Logger.getLogger(Serial_Monitor.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        try (final PrintWriter out = response.getWriter()) {

            out.println(message);
            out.flush();
                    
        }    
    }

}
