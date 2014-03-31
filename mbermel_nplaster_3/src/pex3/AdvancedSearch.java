package pex3;

import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AdvancedSearch extends JPanel implements ListSelectionListener { 
    TextField inputtitle = new TextField(15);
    TextField inputgenre = new TextField(15);
    TextField inputactor = new TextField(15);
    TextField inputrating = new TextField(15);
    TextField inputyear = new TextField(15);
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
    	JLabel yearBox = new JLabel("Length");
    	JLabel emptyBox = new JLabel(" ");
    	JLabel searchInstructions = new JLabel("Title");
    	searchLayout.add(genreBox);
    	searchLayout.add(inputgenre);
    	searchLayout.add(actorBox);
    	searchLayout.add(inputactor);
    	searchLayout.add(ratingBox);
    	searchLayout.add(inputrating);
    	searchLayout.add(yearBox);
    	searchLayout.add(inputyear);
    	searchLayout.add(searchInstructions);
        searchLayout.add(inputtitle);
        searchLayout.add(emptyBox);
        searchLayout.add(enterButton);
        
        enterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                    	// Clear the list model so that it ONLY shows the new results set.
                    	listModel.clear();
                    	java.sql.Connection con;
                        String searchString = inputtitle.getText();
                        String genreString = inputgenre.getText();
                        String actorString = inputactor.getText();
                        String ratingString = inputrating.getText();
                        String lengthString = inputyear.getText();
                        try{
	                        con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
	                        Statement stmt = con.createStatement();
	                        ResultSet rs = stmt.executeQuery( "select * from film_list where title LIKE '%" + searchString + "%' and"
	                        		+ " category like '%"+ genreString+"%' and rating like '%"+ratingString+"%' and actors like '%"+ actorString + "%' and length like '%" + lengthString + "%'");
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
        	      System.out.println(selectedItem + "Debug info from Search.java line 81");
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
