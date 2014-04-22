import java.awt.Component;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class contains two utility methods for selecting
 * files for input or output via the JFileChooser dialog.
 *
 * @author Randall.Bower
 */
public class FileDialogs
{
  // Keep these as static variables so a new dialog is only created once.
  private static JFileChooser openFileChooser = null;
  private static JFileChooser saveFileChooser = null;

  /**
   * Use a dialog box to select a text or java file for input.
   *
   * @param parent A component to use as the parent of this dialog; possibly null.
   * @return A Scanner for the selected file, or null if the file is not
   * found or the user selects Cancel.
   */
  public static Scanner selectInputFile( Component parent )
  {
    if( openFileChooser == null ) // If dialog has never been created, create it.
    {
      if( saveFileChooser == null )
      {
        // Only do this if both file choosers are null (i.e., it hasn't been done).
        setLookAndFeel();
      }
      
      openFileChooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter( "Text/Java Files", "txt", "java" );
      openFileChooser.setFileFilter( filter );
    }
    
    do
    {
      // Let the JFileChooser dialog do its thing.
      int returnVal = openFileChooser.showOpenDialog( parent );
      
      try
      {
        // This will be true if the user clicks the Open button or
        // double-clicks on a file in the dialog.
        if( returnVal == JFileChooser.APPROVE_OPTION )
        {
          // Any errors here (FileNotFound, etc.) will throw an exception,
          // show the message dialog in the catch below, and stay in the
          // outer do-while loop allowing the user to try again.
          return new Scanner( openFileChooser.getSelectedFile() );
        }
        else
        {
          // This will happen if the user clicks the Cancel button.
          return null;
        }
      }
      // Catch all exceptions and show a generic error message. More specific
      // exceptions could be caught to give more useful feedback if desired.
      catch( Exception e )
      {
        JOptionPane.showMessageDialog( parent, "File could not be opened for reading.",
                                       "File Error", JOptionPane.ERROR_MESSAGE );
      }
    } while( true );
  }

  /**
   * Use a dialog box to select a text file for output.
   *
   * Note: This does NOT enforce the ".txt" extension. By default the file
   * chooser will only show ".txt" files, but the user must include the
   * extension in the file name selected/typed.
   *
   * @param parent A component to use as the parent of this dialog; possibly null.
   * @return A PrintStream for the selected file, or null if the file cannot
   * be created for output or the user selects Cancel.
   */
  public static PrintStream selectOutputFile( Component parent )
  {
    if( saveFileChooser == null ) // If dialog has never been created, create it.
    {
      if( openFileChooser == null )
      {
        // Only do this if both file choosers are null (i.e., it hasn't been done).
        setLookAndFeel();
      }
      
      saveFileChooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter( "Text Files", "txt" );
      saveFileChooser.setFileFilter( filter );
    }

    do
    {
      // Let the JFileChooser dialog do its thing.
      int returnVal = saveFileChooser.showSaveDialog( parent );

      try
      {
        // This will be true if the user clicks the Save button or
        // double-clicks on a file in the dialog.
        if( returnVal == JFileChooser.APPROVE_OPTION )
        {
          File selectedFile = saveFileChooser.getSelectedFile();
          
          // This will be true if the user types a new file name into the dialog.
          if( !selectedFile.exists() )
          {
            // Any errors here will throw an exception, show the message dialog in the catch
            // below, and stay in the outer do-while loop allowing the user to try again.
            return new PrintStream( selectedFile );
          }
          // If the file already exists and has something in it, prompt to overwrite.
          else if( selectedFile.isFile() && selectedFile.length() > 0 )
          {
            int option = JOptionPane.showConfirmDialog( saveFileChooser, "File exists. Overwrite?",
                                                        "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION );
            if( option == JOptionPane.YES_OPTION )
            {
              // Any errors here will throw an exception, show the message dialog in the catch
              // below, and stay in the outer do-while loop allowing the user to try again.
              return new PrintStream( selectedFile );
            }
            else if( option == JOptionPane.CANCEL_OPTION )
            {
              // This will happen if the user clicks the Cancel button in the JOptionPane dialog.
              return null;
            }
            // NOTE: If the user clicks No in the JOptionPane dialog asking to overwrite an existing
            // file, neither of the above if-statements will be true and the method will remain in
            // the outer do-while loop allowing the user to select another file.
          }
        }
        else
        {
          // This will happen if the user clicks the Cancel button in the JFileChooser dialog.
          return null;
        }
      }
      // Catch all exceptions and show a generic error message. More specific
      // exceptions could be caught to give more useful feedback if desired.
      catch( Exception e )
      {
        JOptionPane.showMessageDialog( parent, "File could not be opened for writing.",
                                       "File Error", JOptionPane.ERROR_MESSAGE );
      }
    } while( true );
  }
  
  private static void setLookAndFeel()
  {
    // Make GUI look like normal operating system GUI rather than
    // Java's default six-year-old-with-a-crayon look.
    try
    {
      javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );

      // For reference, this would show all of the installed options:
      // for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
      // {
      //   System.out.println( info.getName() + " " + info.getClassName() );
      // }
    }
    catch( Exception e )
    {
      // Ignore exceptions and continue; if this fails for some reason, the GUI
      // will still open with default Java, six-year-old-with-a-crayon look.
      System.err.println( "Problem setting UI." );
    }    
  }
}
