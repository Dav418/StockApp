package Application;

// written by Dawid (Dav) Gorski

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ReadWriteData {
    private static String fileName;

    private static void setFileName(String userName){
        fileName = "accountData\\" + userName +".txt";
    }

    static void writeData(User user){ //saves the user object
        setFileName(user.getUserName());
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(fileName));
            objectOut.writeObject(user);
            objectOut.close();
        } catch (Exception ex) { AlertMessage.infoBox(ex.toString()); }
    }

    User readData(String userName){ //loads the user object
        setFileName(userName);
        User user = null;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            user = (User) is.readObject();
            is.close();
        } catch (Exception e) { AlertMessage.infoBox(e.toString()); }
        return user;
    }

    static void deleteOldFile(String userName){ //deletes old file if the user has changes username or password
        setFileName(userName);
        Path fileToDeletePath = Paths.get(fileName);
        try{
        Files.delete(fileToDeletePath);}
        catch (Exception e){AlertMessage.infoBox(e.toString());}

    }
}
