package xxl;

import java.io.*;
import xxl.User;
import java.util.List;
import java.util.ArrayList;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

// FIXME import classes

/**
 * Class representing a spreadsheet application.
 */
public class Calculator implements Serializable{
    private static final long serialVersionUID = 1L;

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;
    private String _currentFilename = "";
    private List<User> users = new ArrayList<>();

    // FIXME add more fields if needed

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        // Check if the current network has a file associated with it
        if (_currentFilename == "") {
            throw new MissingFileAssociationException();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_currentFilename)))) {
            out.writeObject(this);
        }
    }
    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */

        public void saveAs(String filename) throws FileNotFoundException, IOException, MissingFileAssociationException {
            _currentFilename = filename;
            save();
            }

    public Spreadsheet getSpreadsheet() { return _spreadsheet; }
    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            // Read the serialized object from the file
            Calculator loadedCalculator = (Calculator) in.readObject();

            // Set the fields of the current Calculator based on the loaded object
            this._spreadsheet = loadedCalculator._spreadsheet;
            this._currentFilename = filename;
            this.users = loadedCalculator.users;

        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException("Error loading application state: " + e.getMessage());
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public Spreadsheet importFile(String filename) throws ImportFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read and process the first two lines
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            int numRows = Integer.parseInt(line1.split("=")[1]);
            int numCols = Integer.parseInt(line2.split("=")[1]);
            createnewSpreadsheet(numRows, numCols);
            createUser("root");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                try {
                    Parser parser = new Parser(_spreadsheet, fields);
                } catch (UnrecognizedEntryException e) {
                    throw new ImportFileException(filename, e);
                }


            }
            return _spreadsheet;
            // FIXME open import file and feed entries to new spreadsheet (in a cycle)
            // each entry is inserted with:
            // _spreadsheet.insertContents(/* FIXME produce arguments */);
            // ....
        } catch (IOException e) {
            throw new ImportFileException(filename, e);
        }
    }
    public Spreadsheet createnewSpreadsheet(int rows, int cols){
        _spreadsheet = new Spreadsheet(rows, cols);
        createUser("root");
        return _spreadsheet;
    }
    public boolean createUser(String name) {
        // Check if a user with the same name already exists
        for (User user : users) {
            if (user.getName().equals(name)) {
                return false; // User with the same name already exists
            }
        }

        // Create a new user with the given name
        User newUser = new User(name);

        // Add the new user to the list of users
        users.add(newUser);

        return true; // User created successfully
    }
}