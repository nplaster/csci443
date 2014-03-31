package pex3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

public class Movies extends JPanel implements ListSelectionListener {

	public Movies(int movieID){
		super(new BorderLayout());
		
		DefaultListModel<String> listModel;
	    JList<String> list;
	    
	    listModel = new DefaultListModel<String>();
	    java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            JPanel skeleton = new JPanel(new GridLayout(4,0));
            add(skeleton, BorderLayout.CENTER);
            ResultSet rs = stmt.executeQuery( "select * from film_text where film_id=" +movieID);
            rs.next();
            String title = rs.getString("title");
            JLabel movieTitle = new JLabel(rs.getString("title"));
            add(movieTitle, BorderLayout.NORTH);
            JLabel description = new JLabel("Movie Description:");
            skeleton.add(description);
            JLabel movieDescription = new JLabel(rs.getString("description"));
            skeleton.add(movieDescription);
            
            rs = stmt.executeQuery("select * from film_list where title='" +title+ "'");
            rs.next();
            String[] actorList = rs.getString("actors").replaceAll("^[,\\s]+", "").split(",");
            
            for(int i = 0; i<actorList.length; i++){
            	listModel.addElement(actorList[i]);
            }
            
            list = new JList<String>(listModel);
    		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    		list.addListSelectionListener( this );
            
            JLabel actors = new JLabel("Actors in this film:");
            skeleton.add(actors);
            skeleton.add(new JScrollPane( list ));
            
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
