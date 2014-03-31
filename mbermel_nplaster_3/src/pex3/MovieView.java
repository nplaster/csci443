package pex3;

import java.awt.BorderLayout;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MovieView extends JPanel implements ListSelectionListener {

	public MovieView(){
		super(new BorderLayout());
		DefaultListModel<String> listModel;
	    JList<String> list;
	    
	    listModel = new DefaultListModel<String>();
	    java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery( "select * from film_text");
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
        
        list = new JList<String>(listModel);
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        list.addListSelectionListener( this );
        add( new JScrollPane( list ), BorderLayout.CENTER );
	}
	
	public void displayMovieInfo(int movieID){
		removeAll();
		JPanel movieInfo = new Movies(movieID);
		add(movieInfo, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
