package pex3;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextField;
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

public class AdvancedSearch extends JPanel implements ListSelectionListener{
	TextField inputLine = new TextField(15); 
    Button enterButton = new Button("Search");
    JPanel searchLayout = new JPanel(new GridLayout(0,4));
    JPanel overallLayout = new JPanel(new BorderLayout());
    DefaultListModel<String> listModel;
    JList<String> list;
    
	final JPanel parentLayout = new JPanel();
	listModel = new DefaultListModel<String>();
   	final JLabel searchInstructions = new JLabel("Advanced Search:");
	searchLayout.add(searchInstructions);
    searchLayout.add(inputLine);
    searchLayout.add(enterButton);



    basicSearch.addActionListener(
    		new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    				searchLayout.removeAll();
    				overallLayout.removeAll();
    				parentLayout.removeAll();
    				removeAll();
    				setLayout(null);
    				//remove(searchInstructions);
    				//basicSearch();
    			}
    		});
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
    Button moreInfo = new Button("More Info");
    
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
    parentLayout.add(overallLayout);
    
    add(parentLayout);
    
	
}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
