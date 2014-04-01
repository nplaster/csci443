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
public class AdvancedSearch extends JPanel implements ListSelectionListener {
	// setup all of the text fields for search fields
	TextField inputtitle = new TextField(15);
	TextField inputgenre = new TextField(15);
	TextField inputactor = new TextField(15);
	TextField inputrating = new TextField(15);
	TextField inputyear = new TextField(15);

	// setup layout and buttons for advanced search
	Button enterButton = new Button("Search");
	JPanel searchLayout = new JPanel(new GridLayout(0, 4));
	JPanel overallLayout = new JPanel(new BorderLayout());
	DefaultListModel<String> listModel;
	JList<String> list;

	/**
	 * Constructor which calls a function to setup layout
	 */
	public AdvancedSearch() {
		advancedSearch();
	}

	/**
	 * setups up and performs the advanced search
	 */
	private void advancedSearch() {
		listModel = new DefaultListModel<String>();
		setSize(1000, 1000);

		// setup labels for the search boxes
		JLabel genreBox = new JLabel("Genre");
		JLabel actorBox = new JLabel("Actor");
		JLabel ratingBox = new JLabel("Rating");
		JLabel yearBox = new JLabel("Length");
		JLabel emptyBox = new JLabel(" ");
		JLabel searchInstructions = new JLabel("Title");

		// adding all of the information to display the gui
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

		// listener for the search button
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// Clear the list model so that it ONLY shows the new results
				listModel.clear();
				// setups up sql connection
				java.sql.Connection con;
				// get all of the input from the separate fields
				String searchString = inputtitle.getText();
				String genreString = inputgenre.getText();
				String actorString = inputactor.getText();
				String ratingString = inputrating.getText();
				String lengthString = inputyear.getText();
				try {
					// connect to the sakila database
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/sakila", "root", "");
					Statement stmt = con.createStatement();
					// and the results together for advanced search
					ResultSet rs = stmt
							.executeQuery("select * from film_list where title LIKE '%"
									+ searchString
									+ "%' and"
									+ " category like '%"
									+ genreString
									+ "%' and rating like '%"
									+ ratingString
									+ "%' and actors like '%"
									+ actorString
									+ "%' and length like '%"
									+ lengthString
									+ "%'");
					while (rs.next()) {
						// print results to the screen in list format
						listModel.addElement(rs.getString("title"));
					}

					// close all of the connections
					rs.close();
					stmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
		// add list to the panel displaying on screen
		list = new JList<String>(listModel);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(this);
		overallLayout.add(new JScrollPane(list), BorderLayout.CENTER);
		overallLayout.add(searchLayout, BorderLayout.NORTH);
		// button to take to specific movie page
		final Button moreInfo = new Button("More Info");

		// action performed when specific movie selected
		moreInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = list.getSelectedValue();
				int movieID;
				java.sql.Connection con;
				try {
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/sakila", "root", "");
					Statement stmt = con.createStatement();
					// query the data base for the film that was selected
					ResultSet rs = stmt
							.executeQuery("select * from film_text where title='"
									+ selectedItem + "'");
					rs.next();
					movieID = Integer.parseInt(rs.getString("film_id"));

					// change to the movie pane
					JTabbedPane tp = Home.getTabbedPane();
					MovieView movies = Home.getMovieJPanel();
					// pass in the given movie title
					movies.displayMovieInfo(movieID);
					tp.setSelectedIndex(3);

					// closes all of the connections
					rs.close();
					stmt.close();
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		// prints left button to the screen
		overallLayout.add(moreInfo, BorderLayout.EAST);
		add(overallLayout);

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

	}
}
