package gateway;

import usecases.AppointmentSnapshotManager;
import java.io.*;
import java.util.logging.Logger;

public class AppointmentSnapshotReadWrite {
    private static final Logger logger = Logger.getLogger(ClientUserReadWrite.class.getName());
    AppointmentSnapshotManager asm;

    public AppointmentSnapshotManager createAppointmentSnapshotManagerFromFile(String path) throws ClassNotFoundException {
        readFromFile(path);
        return asm;
    }

    private void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            asm = (AppointmentSnapshotManager) input.readObject();
            input.close();
        } catch (IOException ex) {
            asm = new AppointmentSnapshotManager();
        }
    }

    public static void saveToFile(String path, AppointmentSnapshotManager appointmentSnapshotManager) throws IOException {
        OutputStream file = new FileOutputStream(path);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(appointmentSnapshotManager);
        output.close();
    }

}
