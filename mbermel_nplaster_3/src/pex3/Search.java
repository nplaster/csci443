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


@SuppressWarnings("serial")
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
		listModel = new DefaultListModel<String>();
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
		// Create items. Add to panels.
		list = new JList<String>(listModel);
		this.list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.list.addListSelectionListener( this );
		overallLayout.add( new JScrollPane( list ), BorderLayout.CENTER );
		overallLayout.add(searchLayout, BorderLayout.NORTH);
		final Button moreInfo = new Button("More Info");

		moreInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String selectedItem = list.getSelectedValue();
				int movieID;
				java.sql.Connection con;
				// Populate list box with search results.
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
		
		// Add all components to actual JPanel.
		overallLayout.add(moreInfo, BorderLayout.EAST);
		add(overallLayout);

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

	}
}
