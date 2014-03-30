package pex3;

import java.applet.Applet;
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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Search extends JPanel implements ListSelectionListener { 
    TextField inputLine = new TextField(15); 
    Button enterButton = new Button("Search");
    
    JPanel searchLayout = new JPanel(new GridLayout(0,3));
    JPanel overallLayout = new JPanel(new BorderLayout());
    DefaultListModel<String> listModel;
    JList<String> list;
    
    public Search() {
    	basicSearch();
    }

	private void basicSearch() {
		revalidate();
		listModel = new DefaultListModel<String>();
    	setSize(1000, 1000);
    	JLabel searchInstructions = new JLabel("Basic Search:");
    	searchLayout.add(searchInstructions);
        searchLayout.add(inputLine);
        
        searchLayout.add(enterButton);
        
        enterButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                    	// Clear the list model so that it ONLY shows the new results set.
                    	listModel.clear();
                    	java.sql.Connection con;
                        String s = inputLine.getText();
                        try{
	                        con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sakila", "root", "" );
	                        Statement stmt = con.createStatement();
	                        ResultSet rs = stmt.executeQuery( "select * from film_text where description LIKE '%" + s + "%' or title LIKE '%" + s + "%'");

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
