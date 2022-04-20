package gateway;

import usecases.ClientUserManager;
import usecases.ClientUserSnapshotManager;
import static gateway.FileReadAndWrite.*;

import java.io.*;
import java.util.logging.Logger;

public class ClientUserSnapshotReadWrite {
    ClientUserSnapshotManager csm;
    private ClientUserReadWrite clientUserReadWrite= new ClientUserReadWrite();
    private ClientUserManager clientUserManager = clientUserReadWrite.createClientUserManagerFromFile(CLIENT_USER_FILE);

    public ClientUserSnapshotReadWrite() throws ClassNotFoundException {
    }

    public ClientUserSnapshotManager createClientUserSnapshotManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return csm;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            csm = (ClientUserSnapshotManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            csm = new ClientUserSnapshotManager(clientUserManager);
        }
    }

    public static void saveToFile(String path, ClientUserSnapshotManager csm) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(csm);
        output.close();
    }

}

