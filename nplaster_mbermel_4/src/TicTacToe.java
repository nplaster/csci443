/**
 * A simple GUI to be used to implement asynchronous chat using socket based
 * connections between two machines where one machine acts as the server and
 * the other machine acts as the client.s
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * The main class that controls all communications between client and server As
 * well as handling the logic for the Tic Tac Toe Game
 */
public class TicTacToe implements ActionListener {
	// Somewhat arbitrary, but see the section titled Understanding Ports:
	// http://docs.oracle.com/javase/tutorial/networking/overview/networking.html
	private static final int PORT = 51041;

	// Setting up basic variables
	private TicTacToeServer server;
	private TicTacToeClient client;
	private MessageSender sender;
	private Thread listener;

	private JFrame frame;
	private JPanel ticTacToeGrid;

	private static ArrayList<JButton> buttonList = new ArrayList<JButton>();
	private static boolean isServer = false;
	private static int buttonCount = 0;
	private static boolean myTurn = true;

	/**
	 * Constructor to setup gui
	 */
	public TicTacToe() {
		this.frame = new JFrame("TicTacToe XTREM3: ");
		this.frame.setLayout(new BorderLayout());
		this.frame.setLocation(42, 42);
		this.frame.setForeground(Color.WHITE);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.addWindowListener(new ChatWindowListener());
		this.frame.setSize(100, 100);

		this.ticTacToeGrid = new JPanel(new GridLayout(3, 3));

		// loop to add buttons to the screen
		for (int i = 0; i < 9; i++) {
			JButton b = new JButton();
			b.setActionCommand(Integer.toString(buttonCount));
			buttonCount++;
			b.addActionListener(new buttonListener());
			buttonList.add(b);
			this.ticTacToeGrid.add(buttonList.get(i));
		}

		// set default size of board on screen
		this.ticTacToeGrid.setPreferredSize(new Dimension(400, 400));
		this.frame.getContentPane()
				.add(this.ticTacToeGrid, BorderLayout.CENTER);
		this.frame.pack();

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createHelpMenu());
		this.frame.setJMenuBar(menuBar);

		this.frame.pack();
		this.frame.setVisible(true);
	}

	/**
	 * Function to check to see if the user can make the move or not
	 * 
	 * @param buttonID
	 * @return
	 */
	public boolean checkIfSpaceEmpty(int buttonID) {
		String currentStatus = buttonList.get(buttonID).getText();
		if (currentStatus.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if the current play or made the move won the game or not
	 * 
	 * @return
	 */
	public boolean checkForWin() {

		// check rows
		for (int i = 0; i < 9; i += 3) {
			for (int j = i; j < i + 3; j++) {
				if (isServer && buttonList.get(j).getText() == "X") {
					if (j == i + 2) {
						return true;
					}
					continue;
				} else if (!isServer && buttonList.get(j).getText() == "O") {
					if (j == i + 2) {
						return true;
					}
					continue;
				}
				break;
			}
		}

		// check columns
		for (int i = 0; i < 3; i++) {
			for (int j = i; j < 9; j += 3) {
				if (isServer && buttonList.get(j).getText() == "X") {
					if (j == i + 6) {
						return true;
					}
					continue;
				} else if (!isServer && buttonList.get(j).getText() == "O") {
					if (j == i + 6) {
						return true;
					}
					continue;
				}
				break;
			}
		}

		// check diagonals
		if (buttonList.get(0).getText() == buttonList.get(4).getText()
				&& buttonList.get(4).getText() == buttonList.get(8).getText()
				&& buttonList.get(0).getText() != "") {
			return true;
		} else if (buttonList.get(2).getText() == buttonList.get(4).getText()
				&& buttonList.get(4).getText() == buttonList.get(6).getText()
				&& buttonList.get(2).getText() != "") {
			return true;
		}

		return false;

	}

	/**
	 * creates the file menu in the top right with options to start or join a
	 * game of tictactoe
	 * 
	 * @return
	 */
	private JMenu createFileMenu() {
		FileMenuListener listener = new FileMenuListener();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItem = new JMenuItem("Start TicTacToe Game...",
				KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK));
		menuItem.addActionListener(listener);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Join TicTacToe Game...", KeyEvent.VK_J);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
				ActionEvent.ALT_MASK));
		menuItem.addActionListener(listener);
		fileMenu.add(menuItem);

		fileMenu.addSeparator();
		menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		menuItem.addActionListener(listener);
		fileMenu.add(menuItem);

		return fileMenu;
	}

	/**
	 * Class that listens for buttons pressed on the screen
	 */
	private class buttonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// switch statement for printing x or o in specified spot on screen
			if (myTurn && sender != null) {
				if (checkIfSpaceEmpty(Integer.parseInt(e.getActionCommand()))) {
					myTurn = false;
					sender.sendMessage("Your turn.");
					if (e.getActionCommand().equals("0")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("0");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("0");
						}
					} else if (e.getActionCommand().equals("1")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("1");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("1");
						}
					} else if (e.getActionCommand().equals("2")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("2");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("2");
						}
					} else if (e.getActionCommand().equals("3")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("3");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("3");
						}
					} else if (e.getActionCommand().equals("4")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("4");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("4");
						}
					} else if (e.getActionCommand().equals("5")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("5");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("5");
						}
					} else if (e.getActionCommand().equals("6")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("6");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("6");
						}
					} else if (e.getActionCommand().equals("7")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("7");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("7");
						}
					} else if (e.getActionCommand().equals("8")) {
						if (isServer) {
							((AbstractButton) e.getSource()).setText("X");
							sender.sendMessage("8");
						} else {
							((AbstractButton) e.getSource()).setText("O");
							sender.sendMessage("8");
						}
					} else {
						System.err.print("Unknown Button Error");
					}
					if (checkForWin()) {
						myTurn = false;
						sender.sendMessage("You lose!");
						JOptionPane.showOptionDialog(null, "You win!",
								"Congrats!", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, null,
								null);
						System.exit(1);
					}
				}
			} else if (!myTurn) {
				JOptionPane.showOptionDialog(null,
						"Didn't your mom ever teach you how to take turns?",
						"Come on now...", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, null, null);
			} else {
				JOptionPane
						.showOptionDialog(
								null,
								"Please start a new game or join a game first!\nFile >> Start/Join TicTacToe Game ",
								"Information", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, null,
								null);
			}
		}
	}

	/**
	 * Listener for seeing if a game is started or joined
	 */
	private class FileMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// Switch statements with Strings is new in JDK 7.
			switch (ae.getActionCommand()) {
			case "Start TicTacToe Game...":
				server = new TicTacToeServer();
				sender = server;
				listener = new Thread(server);
				listener.start();
				break;
			case "Join TicTacToe Game...":
				client = new TicTacToeClient();
				sender = client;
				listener = new Thread(client);
				listener.start();
				break;
			case "Exit":
				System.exit(0);
				break;
			}
		}
	}

	/**
	 * Creates the the help menu with the about section
	 * 
	 * @return
	 */
	private JMenu createHelpMenu() {
		HelpMenuListener listener = new HelpMenuListener();

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		JMenuItem menuItem = new JMenuItem("About...", KeyEvent.VK_A);
		helpMenu.add(menuItem);
		menuItem.addActionListener(listener);

		return helpMenu;
	}

	/**
	 * Listens to see if anything in the help menu was pressed and displays
	 * accordingly
	 */
	private class HelpMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			String message = null;
			// Switch statements with Strings is new in JDK 7.
			switch (ae.getActionCommand()) {
			case "About...":
				message = "Tic Tac Toe\n\nCS 443, Spring 2014\n\nAuthor: Us\n\n";
				break;
			}
			JOptionPane.showMessageDialog(frame, message,
					ae.getActionCommand(), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Required for classes
	 * @param arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

	/**
	 * Main function that handles the client and server side actions
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// Make GUI look like normal operating system GUI rather than
		// Java's default six-year-old-with-a-crayon look.
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore exceptions and continue; if this fails for some reason,
			// the GUI
			// will still open with default Java, six-year-old-with-a-crayon
			// look.
			System.err.println("Problem setting UI.");
		}

		/*
		 * Create the GUI. For thread safety, this method should be invoked from
		 * the event-dispatching thread. See
		 * http://docs.oracle.com/javase/7/docs
		 * /api/javax/swing/package-summary.html#threading
		 */
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new TicTacToe();
				}
			});
		} catch (Exception e) {
			System.err
					.println("ERROR: createGUI() did not complete successfully.");
			e.printStackTrace();
		}
	}

	/*
	 * Creating an interface with both TicTacToeServer and TicTacToeClient
	 * implementing the interface so both can send messages.
	 */
	private interface MessageSender {
		public void sendMessage(String message);
	}

	/*
	 * Adapted from
	 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html
	 */
	private class TicTacToeServer implements Runnable, MessageSender {
		private PrintWriter output;

		public TicTacToeServer() {
			TicTacToe.isServer = true;
		}

		public void sendMessage(String message) {
			this.output.println(message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				JOptionPane.showMessageDialog(null,
						"Tell sender to connect to "
								+ InetAddress.getLocalHost().getHostAddress()
								+ " ...");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}
			try (ServerSocket serverSocket = new ServerSocket(PORT);
					Socket clientSocket = serverSocket.accept(); // Blocks until
																	// a
																	// connection
																	// is made.

					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(
							clientSocket.getOutputStream(), true);) {
				// Save a reference to the output connection for use by
				// sendMessage method.
				this.output = out;

				// Sending messages will happen on the GUI thread by calling the
				// sendMessage method.
				// Thus, this thread only needs to listen for messages and
				// display them.
				String message;
				do {
					message = in.readLine(); // Blocks until a message is
												// received.

					if (message.equals("Your turn.")) {
						myTurn = true;
					} else if (!myTurn) {
						continue;
					}
					boolean tradeTurns = message.equals("Your turn.")
							|| message.equals("You lose!");
					// Switch statement for printing on the screen in correct
					// place
					if (!tradeTurns) {
						int value = Integer.parseInt(message);
						switch (value) {
						case 0:
							TicTacToe.buttonList.get(0).setText("O");
							break;
						case 1:
							TicTacToe.buttonList.get(1).setText("O");
							break;
						case 2:
							TicTacToe.buttonList.get(2).setText("O");
							break;
						case 3:
							TicTacToe.buttonList.get(3).setText("O");
							break;
						case 4:
							TicTacToe.buttonList.get(4).setText("O");
							break;
						case 5:
							TicTacToe.buttonList.get(5).setText("O");
							break;
						case 6:
							TicTacToe.buttonList.get(6).setText("O");
							break;
						case 7:
							TicTacToe.buttonList.get(7).setText("O");
							break;
						case 8:
							TicTacToe.buttonList.get(8).setText("O");
							break;

						}
					}
				} while (!message.equalsIgnoreCase("You lose!"));

				// Send the "GoodBye" message back to stop the other thread.
				JOptionPane.showMessageDialog(null, "Sorry, you lose!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/*
	 * Adapted from
	 * http://docs.oracle.com/javase/tutorial/networking/sockets/index.html
	 */
	private class TicTacToeClient implements Runnable, MessageSender {
		private PrintWriter output;

		/*
		 * (non-Javadoc)
		 * 
		 * @see TicTacToe.MessageSender#sendMessage(java.lang.String)
		 */
		public void sendMessage(String message) {
			this.output.println(message);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			String ip = JOptionPane.showInputDialog(frame,
					"Enter IP address for server:", "HelloClient",
					JOptionPane.QUESTION_MESSAGE);

			// For debugging when both server and client are on the same
			// machine...
			if (ip == null || ip.isEmpty()) {
				try {
					ip = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}

			try (Socket clientSocket = new Socket(ip, PORT);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(
							clientSocket.getOutputStream(), true);) {
				// Save a reference to the output connection for use by
				// sendMessage method.
				this.output = out;

				// Sending messages will happen on the GUI thread by calling the
				// sendMessage method.
				// Thus, this thread only needs to listen for messages and
				// display them.
				String message;
				do {
					message = in.readLine(); // Blocks until a message is
												// received.

					if (message.equals("Your turn.")) {
						myTurn = true;
					} else if (!myTurn) {
						continue;
					}
					boolean tradeTurns = message.equals("Your turn.")
							|| message.equals("You lose!");
					// Switch statement that prints the X on the appropriate
					// place on the board
					if (!tradeTurns) {
						int value = Integer.parseInt(message);
						switch (value) {
						case 0:
							TicTacToe.buttonList.get(0).setText("X");
							break;
						case 1:
							TicTacToe.buttonList.get(1).setText("X");
							break;
						case 2:
							TicTacToe.buttonList.get(2).setText("X");
							break;
						case 3:
							TicTacToe.buttonList.get(3).setText("X");
							break;
						case 4:
							TicTacToe.buttonList.get(4).setText("X");
							break;
						case 5:
							TicTacToe.buttonList.get(5).setText("X");
							break;
						case 6:
							TicTacToe.buttonList.get(6).setText("X");
							break;
						case 7:
							TicTacToe.buttonList.get(7).setText("X");
							break;
						case 8:
							TicTacToe.buttonList.get(8).setText("X");
							break;

						}
					}
				} while (!message.equalsIgnoreCase("You lose!"));

				JOptionPane.showMessageDialog(null, "Sorry, you lose!");

				System.exit(1);
			} catch (UnknownHostException e) {
				System.err.println("Couldn't connect to: " + ip);
				e.printStackTrace();
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: "
						+ ip);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * The listener for the chat window
	 */
	private class ChatWindowListener implements WindowListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent
		 * )
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			// Send the "GoodBye" message to stop the other thread.
			if (sender != null)
				sender.sendMessage("GoodBye");
			if (listener == null) {
				System.exit(0);
			}
			// Wait for the listener thread (which may be either a server or
			// client) to stop,
			// which happens when the "GoodBye" message is sent and returned.
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			while (listener.isAlive()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) { /* Ignore interrupt. */
				}
			}
			System.exit(0);
		}

		// These methods are not used.
		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	}
}