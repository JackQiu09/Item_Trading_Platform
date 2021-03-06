package gateway;

import usecases.ClientUserManager;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.*;

public class ClientUserReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    ClientUserManager cm;

    public ClientUserManager createClientUserManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return cm;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            cm = (ClientUserManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public static void saveToFile(String path, ClientUserManager clientUserManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(clientUserManager);
        output.close();
    }
}
