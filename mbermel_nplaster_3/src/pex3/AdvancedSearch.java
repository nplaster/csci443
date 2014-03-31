package pex3;

import java.awt.*;
import java.awt.event.*;
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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AdvancedSearch extends JPanel implements ListSelectionListener { 
    TextField inputLine = new TextField(15);
    TextField inputLine1 = new TextField(15);
    TextField inputLine2 = new TextField(15);
    TextField inputLine3 = new TextField(15);
    TextField inputLine4 = new TextField(15);
    Button enterButton = new Button("Search");
    
    JPanel searchLayout = new JPanel(new GridLayout(0,4));
    JPanel overallLayout = new JPanel(new BorderLayout());
    DefaultListModel<String> listModel;
    JList<String> list;
    
    public AdvancedSearch() {
    	advancedSearch();
    }

	private void advancedSearch() {
		listModel = new DefaultListModel<String>();
    	setSize(1000, 1000);
    	JLabel genreBox = new JLabel("Genre");
    	JLabel actorBox = new JLabel("Actor");
    	JLabel ratingBox = new JLabel("Rating");
    	JLabel yearBox = new JLabel("Year");
    	JLabel emptyBox = new JLabel(" ");
    	JLabel searchInstructions = new JLabel("Title");
    	searchLayout.add(genreBox);
    	searchLayout.add(inputLine1);
    	searchLayout.add(actorBox);
    	searchLayout.add(inputLine2);
    	searchLayout.add(ratingBox);
    	searchLayout.add(inputLine3);
    	searchLayout.add(yearBox);
    	searchLayout.add(inputLine4);
    	searchLayout.add(searchInstructions);
        searchLayout.add(inputLine);
        searchLayout.add(emptyBox);
        searchLayout.add(enterButton);
        
        enterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                    	// Clear the list model so that it ONLY shows the new results set.
                    	listModel.clear();
                    	java.sql.Connection con;
                        String searchString = inputLine.getText();
                        String genreString = inputLine1.getText();
                        String actorString = inputLine2.getText();
                        String ratingString = inputLine3.getText();
                        String yearString = inputLine4.getText();
                        try{
	                        con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
	                        Statement stmt = con.createStatement();
	                        ResultSet rs = stmt.executeQuery( "select * from film_text where description LIKE '%" + searchString + "%' or title LIKE '%" + searchString + "%'");

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
                }
             );
        list = new JList<String>(listModel);
        this.list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        this.list.addListSelectionListener( this );
        overallLayout.add( new JScrollPane( list ), BorderLayout.CENTER );
        overallLayout.add(searchLayout, BorderLayout.NORTH);
        final Button moreInfo = new Button("More Info");
        
        moreInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// This method can be called only if
				// there's a valid selection
				// so go ahead and remove whatever's selected.
				String selectedItem = list.getSelectedValue();
				int movieID;
				java.sql.Connection con;
				try{
					con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery( "select * from film_text where title='" + selectedItem+"'");
					rs.next();
					movieID = Integer.parseInt(rs.getString("film_id"));
					
					JTabbedPane tp = Home.getTabbedPane();
					MovieView movies = Home.getMovieJPanel();
					movies.displayMovieInfo(movieID);
					tp.setSelectedIndex(3);

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
        overallLayout.add(moreInfo, BorderLayout.EAST);
        add(overallLayout);
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
