package gateway;

import entities.*;
import usecases.AdminUserManager;
import usecases.ClientUserManager;
import usecases.ItemListManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gateway.FileReadAndWrite.*;

public class AdminUserReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    AdminUserManager am;

    public AdminUserManager createClientUserManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return am;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            am = (AdminUserManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
    }

    public void saveToFile(String path, AdminUserManager adminUserManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(adminUserManager);
        output.close();
    }


}
