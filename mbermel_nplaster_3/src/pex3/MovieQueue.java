package pex3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextArea;
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
import javax.swing.ListSelectionModel;
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
		setup();
		
		add(parentFrame);
	}
	
	public void setup(){
		listModel = new DefaultListModel<String>();
		buttonFrame.add(refresh);
		refresh.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        	      refresh();
        	      }
        });
		buttonFrame.add(up);
		up.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        	      up();
        	      }
        });
		buttonFrame.add(down);
		down.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        	      down();
        	      }
        });
		buttonFrame.add(remove);
		remove.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        	      remove();
        	      }
        });
		parentFrame.add(buttonFrame, BorderLayout.EAST);
		listModel.clear();
    	java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            stmt.executeUpdate( "drop table if exists renamestate" );
            stmt.executeUpdate( "create table renamestate(position integer, title varchar(50))" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (1,'Academy Dinosaur')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (2,'Anything Savannah')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (3,'Beast Hunchback')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (4,'Zorro Ark')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (5,'Caribbean Liberty')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (6,'Wild Apollo')" );
            stmt.executeUpdate( "insert into renamestate (position, title) values (7,'Boulevard Mob')" );
            ResultSet rs = stmt.executeQuery( "select * from renamestate order by position" );
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
        this.list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        this.list.addListSelectionListener( this );
        parentFrame.add( new JScrollPane( list ), BorderLayout.CENTER );
	
	}
	
	public void refresh(){
		listModel.clear();
    	java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( "select * from renamestate order by position" );

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
	
	public void up(){
		
	}
	
	public void down(){
		
	}
	
	public void remove(){
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	


}
