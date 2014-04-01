package pex3;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * The main java applet that is first launched
 */
@SuppressWarnings("serial")
public class Home extends Applet {
	// setup all of the things needed for a tabbed pane layout
	protected static JTabbedPane tabbedPane;
	protected JPanel search;
	protected JPanel advancedSearch;
	protected JPanel movieQueue;
	protected static ActorView actors;
	protected static MovieView movies;

	/**
	 * the layout structure class with a jframe to house all of the jpanels
	 */
	public class LayoutStructure extends JFrame {

		/**
		 * default constructor to setup the tabbed pane layout
		 */
		private LayoutStructure() {
			// setup main layout
			setTitle("Movie Queue [like Netflixs]");
			setSize(600, 600);
			setBackground(Color.gray);

			// create panel for the tabbed panes
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout());
			getContentPane().add(topPanel);

			// create each of the pages
			createMovieQueuePage();
			createSearchPage();
			createAdvancedSearchPage();
			createActorsPage();
			createMoviesPage();

			// setup up the specific panes
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Search", search);
			tabbedPane.addTab("Advanced Search", advancedSearch);
			tabbedPane.addTab("Movie Queue", movieQueue);
			tabbedPane.addTab("Movies", movies);
			tabbedPane.addTab("Actors", actors);

			topPanel.add(tabbedPane, BorderLayout.CENTER);
		}

		/**
		 * creates the advanced search page
		 */
		public void createAdvancedSearchPage() {
			advancedSearch = new AdvancedSearch();
		}

		/**
		 * creates the basic search page
		 */
		public void createSearchPage() {
			search = new Search();
		}

		/**
		 * creates the movie queue page
		 */
		public void createMovieQueuePage() {
			movieQueue = new MovieQueue();
		}

		/**
		 * creates the actors list page
		 */
		public void createActorsPage() {
			actors = new ActorView();
		}

		/**
		 * creates the movies list page
		 */
		public void createMoviesPage() {
			movies = new MovieView();
		}

	}

	/**
	 * @returns the current pane in selection
	 */
	public static JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * @returns the movie pane
	 */
	public static MovieView getMovieJPanel() {
		return movies;
	}

	/**
	 * @returns the actors pane
	 */
	public static ActorView getActorJPanel() {
		return actors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		LayoutStructure layout = new LayoutStructure();
		layout.setVisible(true);
	}
}
