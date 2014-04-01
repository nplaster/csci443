package pex3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Actors class is a JPanel that displays an individual actor and the movies
 * that they appeared in
 */
@SuppressWarnings("serial")
public class Actors extends JPanel implements ListSelectionListener {
	JList<String> list;

	/**
	 * Constructor which takes the selected actor and displays the information
	 * 
	 * @param actorID
	 *            - the selected actor from a movies list
	 */
	public Actors(int actorID) {
		super(new BorderLayout());
		// setup sql connection
		java.sql.Connection con;
		try {
			// connect to sql database
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();

			// setup skeleton for the page
			JPanel skeleton = new JPanel(new GridLayout(2, 0));
			add(skeleton, BorderLayout.CENTER);

			// query the database for the actor id
			ResultSet rs = stmt
					.executeQuery("select * from actor_info where actor_id="
							+ actorID);
			rs.next();

			// make a label to print the actors first and last name to the
			// screen
			JLabel actorName = new JLabel(rs.getString("first_name") + " "
					+ rs.getString("last_name"));
			add(actorName, BorderLayout.NORTH);

			// label for movies
			JLabel movieList = new JLabel("Movies Played In");
			skeleton.add(movieList);

			// display the list of all the movies wrapped into a text box
			JTextArea moviesPlayedIn = new JTextArea(rs.getString("film_info"));
			moviesPlayedIn.setEditable(false);
			moviesPlayedIn.setLineWrap(true);
			skeleton.add(moviesPlayedIn);

			// close all connections
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {

	}

}
