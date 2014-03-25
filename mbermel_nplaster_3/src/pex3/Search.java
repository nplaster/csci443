package pex3;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mysql.jdbc.Connection;


public class Search extends Applet  { 
    TextField inputLine = new TextField(15); 
    Button enterButton = new Button("Enter");
    JPanel p = new JPanel();
    
    public Search() {
    	setSize(1000, 500);
    	JLabel searchInstructions = new JLabel("Please enter your query.");
    	p.add(searchInstructions);
        p.add(inputLine);
        
        p.add(enterButton);
        enterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                    	java.sql.Connection con;
                        String s = inputLine.getText();
                        try{
	                        con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
	                        Statement stmt = con.createStatement();
	                        ResultSet rs = stmt.executeQuery( "select * from film_text where description LIKE '%" + s + "%' or title LIKE '%" + s + "%'");

	                        while( rs.next() )
	                        {
	                          System.out.println( rs.getString( "title" ) );
	                        }
	                        
	                        rs.close();
	                        stmt.close();
	                        con.close();
                        }
                        catch( SQLException e )
                        {
                          e.printStackTrace();
                        }
                        
                    }
                }
             );
        add(p);
    }
}
