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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class MovieQueue extends JPanel implements ListSelectionListener {
	// Create J-Components used in this JPanel
	DefaultListModel<String> listModel;
	JList<String> list;

	JPanel buttonFrame = new JPanel(new GridLayout(4, 0));
	Button refresh = new Button("Refresh");
	Button up = new Button("Up");
	Button down = new Button("Down");
	Button remove = new Button("Remove");

	public MovieQueue() {
		super(new BorderLayout());
		setup();

	}

	public void setup() {
		// Add action listeners to all the buttons.
		listModel = new DefaultListModel<String>();
		buttonFrame.add(refresh);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		buttonFrame.add(up);
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = list.getSelectedValue();
				up(selectedItem);
			}
		});
		buttonFrame.add(down);
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = list.getSelectedValue();
				down(selectedItem);
			}
		});
		buttonFrame.add(remove);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = list.getSelectedValue();
				remove(selectedItem);

			}
		});
		// Clear the list model before re-populating to avoid duplicates.
		listModel.clear();
		java.sql.Connection con;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();
			// Delete the movie queue table if it already exists.
			stmt.executeUpdate("drop table if exists Arkansas_Queue");
			// Re-create movie queue table.
			stmt.executeUpdate("create table Arkansas_Queue(position integer, title varchar(50))");
			// Populate table with default data.
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (1,'Academy Dinosaur')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (2,'Anything Savannah')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (3,'Beast Hunchback')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (4,'Zorro Ark')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (5,'Caribbean Liberty')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (6,'Wild Apollo')");
			stmt.executeUpdate("insert into Arkansas_Queue (position, title) values (7,'Boulevard Mob')");
			// Order list by position in queue
			ResultSet rs = stmt
					.executeQuery("select * from Arkansas_Queue order by position");
			while (rs.next()) {
				// Populate list model
				listModel.addElement(rs.getString("title"));
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		list = new JList<String>(listModel);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(this);
		// Place the list in the center of the screen
		add(new JScrollPane(list), BorderLayout.CENTER);
		// Place buttons to the right of the screen
		add(buttonFrame, BorderLayout.EAST);

	}

	// This function refreshes the contents of the JPanel
	public void refresh() {
		// Clear list model before repopulating
		listModel.clear();
		java.sql.Connection con;
		// Repopulate list model
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from Arkansas_Queue order by position");

			while (rs.next()) {
				listModel.addElement(rs.getString("title"));
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Move item up in queue
	public void up(String titleString) {
		listModel.clear();
		java.sql.Connection con;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();
			ResultSet current = stmt
					.executeQuery("select position from Arkansas_Queue where title='"
							+ titleString + "'");
			int newnum = 0;
			int previous = 0;
			while (current.next()) {
				newnum = current.getInt(1);
				previous = newnum;
				newnum = newnum - 1;
			}
			stmt.executeUpdate("update Arkansas_Queue set position=" + previous
					+ " where position=" + newnum);
			stmt.executeUpdate("update Arkansas_Queue set position=" + newnum
					+ " where title='" + titleString + "'");
			ResultSet rs = stmt
					.executeQuery("select * from Arkansas_Queue order by position");

			while (rs.next()) {
				listModel.addElement(rs.getString("title"));
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Move item down in queue
	public void down(String titleString) {
		listModel.clear();
		java.sql.Connection con;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();
			ResultSet current = stmt
					.executeQuery("select position from Arkansas_Queue where title='"
							+ titleString + "'");
			int newnum = 0;
			int previous = 0;
			while (current.next()) {
				newnum = current.getInt(1);
				previous = newnum;
				newnum = newnum + 1;
			}
			stmt.executeUpdate("update Arkansas_Queue set position=" + previous
					+ " where position=" + newnum);
			stmt.executeUpdate("update Arkansas_Queue set position=" + newnum
					+ " where title='" + titleString + "'");
			ResultSet rs = stmt
					.executeQuery("select * from Arkansas_Queue order by position");

			while (rs.next()) {
				listModel.addElement(rs.getString("title"));
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Remove item from queue
	public void remove(String titleString) {
		listModel.clear();
		java.sql.Connection con;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sakila", "root", "");
			Statement stmt = con.createStatement();
			stmt.executeUpdate("delete from Arkansas_Queue where title='"
					+ titleString + "'");
			ResultSet rs = stmt
					.executeQuery("select * from Arkansas_Queue order by position");

			while (rs.next()) {
				listModel.addElement(rs.getString("title"));
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
