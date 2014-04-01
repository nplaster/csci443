package pex3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Actors extends JPanel implements ListSelectionListener {
	JList<String> list;
	public Actors(int actorID){
		super(new BorderLayout());
		
		DefaultListModel<String> listModel;
	    
	    
	    listModel = new DefaultListModel<String>();
	    java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            JPanel skeleton = new JPanel(new GridLayout(2,0));
            add(skeleton, BorderLayout.CENTER);
            ResultSet rs = stmt.executeQuery( "select * from actor_info where actor_id=" + actorID);
            rs.next();
            
            JLabel actorName = new JLabel(rs.getString("first_name") + " " + rs.getString("last_name"));
            add(actorName, BorderLayout.NORTH);
            JLabel movieList = new JLabel("Movies Played In");
            skeleton.add(movieList);
            JTextArea moviesPlayedIn = new JTextArea(rs.getString("film_info"));
            moviesPlayedIn.setEditable(false);
            moviesPlayedIn.setLineWrap(true);
            skeleton.add(moviesPlayedIn);
            
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
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
