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

public class Movies extends JPanel implements ListSelectionListener {
	JList<String> list;
	public Movies(int movieID){
		super(new BorderLayout());
		
		DefaultListModel<String> listModel;
	    
	    
	    listModel = new DefaultListModel<String>();
	    java.sql.Connection con;
        try{
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
            Statement stmt = con.createStatement();
            JPanel skeleton = new JPanel(new GridLayout(4,0));
            JPanel actingPanel = new JPanel(new GridLayout(0,2));
            add(skeleton, BorderLayout.CENTER);
            ResultSet rs = stmt.executeQuery( "select * from film_text where film_id=" +movieID);
            rs.next();
            final String title = rs.getString("title");
            JPanel northPanelWithAddButton = new JPanel(new GridLayout(0,2));
            JPanel northPanelWithRemoveButton = new JPanel(new GridLayout(0,2));
            JLabel movieTitle = new JLabel(rs.getString("title"));
            

            Button addToQueue = new Button("Add to Queue");
            Button removeFromQueue = new Button("Remove from Queue");
            
            addToQueue.addActionListener(
    				new ActionListener() {
    					public void actionPerformed(ActionEvent event) {
    						java.sql.Connection con;
    						try{
    							con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
    							Statement stmt = con.createStatement();
    							ResultSet colcount = stmt.executeQuery("select count(title) from Arkansas_Queue");
    							colcount.next();
    							int number = colcount.getInt(1);
    							number = number + 1;
    							stmt.executeUpdate( "insert into Arkansas_Queue (position, title) values ("+number+" ,'"+title+"')" );

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
            
            
            removeFromQueue.addActionListener(
    				new ActionListener() {
    					public void actionPerformed(ActionEvent event) {
    						// Clear the list model so that it ONLY shows the new results set.
    						java.sql.Connection con;
    						try{
    							con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
    							Statement stmt = con.createStatement();
    							stmt.executeUpdate( "delete from Arkansas_Queue where title='"+title+"'");

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
            
            ResultSet isMovieInQueue = stmt.executeQuery( "select * from Arkansas_Queue where title='" + title + "'");
            
            if(!isMovieInQueue.isBeforeFirst()){
            	northPanelWithAddButton.add(movieTitle);
            	northPanelWithAddButton.add(addToQueue);
            	add(northPanelWithAddButton, BorderLayout.NORTH);
            }
            else{
            	northPanelWithRemoveButton.add(movieTitle);
            	northPanelWithRemoveButton.add(removeFromQueue);
            	add(northPanelWithRemoveButton, BorderLayout.NORTH);
            }
            
            rs = stmt.executeQuery("select * from film_list where title='" +title+ "'");
            rs.next();
            JLabel description = new JLabel("Movie Description:");
            skeleton.add(description);
            JTextArea movieDescription = new JTextArea(rs.getString("description"));
            movieDescription.setEditable(false);
            movieDescription.setLineWrap(true);
            skeleton.add(movieDescription);
            
            
            String[] actorList = rs.getString("actors").replaceAll("^[,\\s]+", "").split(",");
            
            for(int i = 0; i<actorList.length; i++){
            	String trimmedActorsName = actorList[i].replaceAll("^[,\\s]+", "");
            	listModel.addElement(trimmedActorsName);
            }
            
            list = new JList<String>(listModel);
    		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    		list.addListSelectionListener( this );
            
            JLabel actors = new JLabel("Actors in this film:");
            skeleton.add(actors);
            actingPanel.add(new JScrollPane(list));
            Button actorInfo = new Button("Actor Information");
            
            actorInfo.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e) {
    				// This method can be called only if
    				// there's a valid selection
    				// so go ahead and remove whatever's selected.
    				String selectedItem = list.getSelectedValue();
    				String firstName = selectedItem.substring(0,selectedItem.indexOf(" "));
    				String lastName = selectedItem.substring(selectedItem.indexOf(" ") +1, selectedItem.length());
    				int movieID;
    				java.sql.Connection con;
    				try{
    					con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
    					Statement stmt = con.createStatement();
    					ResultSet rs = stmt.executeQuery( "select * from actor_info where (first_name='" + firstName + "'" + "AND last_name='" + lastName + "')");
    					rs.next();
    					int actorID = Integer.parseInt(rs.getString("actor_id"));
    					
    					JTabbedPane tp = Home.getTabbedPane();
    					ActorView actors = Home.getActorJPanel();
    					actors.displayActorInfo(actorID);
    					tp.setSelectedIndex(4);

    					rs.close();
    					stmt.close();
    					con.close();
    				}
    				catch( SQLException e1 )
    				{
    					e1.printStackTrace();
    				}
    				
    				
    				
    				
    				
    			}
    		});
            
            
            actingPanel.add(actorInfo);
            skeleton.add(actingPanel);
            
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
