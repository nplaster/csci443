package pex3;

import java.awt.BorderLayout;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Actor View is a simple list of all the actors and sets up the tabbed pan
 * initially but is over written by the actor class when a specific actor is
 * chosen
 */
@SuppressWarnings("serial")
public class ActorView extends JPanel implements ListSelectionListener {

	/**
	 * Default constructor that setups up a list of actors first and last name
	 */
	public ActorView() {
		super(new BorderLayout());
		// setup variables for listing the actors
		DefaultListModel<String> listModel;
		JList<String> list;
		listModel = new DefaultListModel<String>();

		// setup the sql connection
		java.sql.Connection con;
		try {
			// setup up the connection with the sakila database
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();

			// query the data base for all of the actors from the entire
			// database
			ResultSet rs = stmt.executeQuery("select * from actor");
			while (rs.next()) {
				// split actors up by first and last name on the same line
				String fullname = rs.getString("first_name") + " "
						+ rs.getString("last_name");
				listModel.addElement(fullname);
			}

			// close all connections
			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// add all items to list and display in the middle
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	/**
	 * Calls the actor class to display the specific actor
	 * 
	 * @param actorID
	 *            - the current selected actor
	 */
	public void displayActorInfo(int actorID) {
		// removes everything from the current panel
		removeAll();
		// creates the new panel
		JPanel actor = new Actors(actorID);
		// places back on the screen and repaints it
		add(actor, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
	}
}
