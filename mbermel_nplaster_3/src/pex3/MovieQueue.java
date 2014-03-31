package pex3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MovieQueue extends JPanel implements ListSelectionListener{
	DefaultListModel<String> listModel;
    JList<String> list;
    JPanel parentFrame = new JPanel( new BorderLayout() );
    JPanel buttonFrame = new JPanel( new GridLayout(4, 0) );
    Button refresh = new Button("Refresh");
    Button up = new Button("Up");
    Button down = new Button("Down");
    Button remove = new Button("Remove");
    
	
	public MovieQueue(){
		listModel = new DefaultListModel<String>();
		buttonFrame.add(refresh);
		buttonFrame.add(up);
		buttonFrame.add(down);
		buttonFrame.add(remove);
		
		parentFrame.add(buttonFrame, BorderLayout.EAST);
		
		add(parentFrame);
	}
	
	public void refresh(){
		listModel.clear();
    	java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            // Add movieQueueTable to database!!!!
            ResultSet rs = stmt.executeQuery( "select * from movieQueueTable");

            while( rs.next() )
            {
              listModel.addElement( rs.getString( "title" ) );
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

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	


}
