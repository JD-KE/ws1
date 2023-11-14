import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingCartDB {
    private static File db;
    private static List<String> shoppingCart = new ArrayList<>();
    private static String currentUser;

    public static void setdb (File file) {
        db = file;
    }

    public static List<String> load(String user) {

        // set current user, and clears cart of any leftover content
        currentUser = user;
        shoppingCart.clear();

        File userFile = new File(db, String.format("%s.db",user));
        
        // create new user .db file if does not exist and write path into first line
        if (!userFile.exists() || !userFile.isFile()) {

            try (FileWriter writer = new FileWriter(userFile)){
                BufferedWriter bw = new BufferedWriter(writer);
                userFile.createNewFile();
                bw.write(userFile.toString());
                bw.flush();
            } catch (IOException e) {
                System.err.println("An error occurred when logging in as new user.");
                // e.printStackTrace();
            }
        }

        // reads user file for items and returns list to
        try (Scanner sc = new Scanner(userFile);) {
            
            while(sc.hasNextLine()) {              
                shoppingCart.add(sc.nextLine());
            }

            /* In case existing file does not have path in first line;
            encountered indexOutOfBounds exception during testing when a few blank files was produced thanks
            to weird things happening when closing with try with resources */
            if (shoppingCart.size() != 0){
                shoppingCart.remove(0);
            }
            
            if (shoppingCart.size() == 0) {
                System.out.printf("%s, your cart is empty%n", currentUser);
            } else {
                System.out.printf("%s, your cart contains the following items%n", currentUser);
                for (int i = 0; i < shoppingCart.size(); i++) {
                    System.out.printf("%d. %s\n", (i+1), shoppingCart.get(i));
                }
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("An error occurred when loggin in: user's cart cannot be read.");
            // e.printStackTrace();
        }

        return shoppingCart;
    }

    public static void save (List<String> shoppingCart) {
        //checks if there is a user logged in; if so, writes cart into user file
        if (currentUser == null) {
            System.out.println("Please log in as a user before saving.");
        } else {
            File userFile = new File(db, String.format("%s.db",currentUser));

            try (FileWriter writer = new FileWriter(userFile, false);) {
                BufferedWriter bw = new BufferedWriter(writer);
                
                bw.write(String.format("%s%n",userFile.toString()));

                for (String item : shoppingCart) {
                    bw.write(String.format("%s%n", item));
                }

                bw.flush();

                System.out.println("Your cart has been saved.");

                // remove current user and clear stored cart in helper class
                currentUser = null;
                shoppingCart.clear();

            } catch (IOException e) {
                System.err.println("An error occurred when saving.");
                // e.printStackTrace();
            }
        }
    }

    public static void users() {
        System.out.println("The following users are registered");
        // get array of files
        File [] files = db.listFiles();

        // check if files end with .db, then get users' names from file name and print
        // use external index to deal with gap in number order in case got files that are not .db files
        int count = 1;
        for (int i = 0; i < files.length; i++) {
            String extName = files[i].getName();
            if (extName.endsWith(".db")) {
                System.out.printf("%d. %s%n", (count), extName.substring(0, (extName.length() - 3)));
                count++;
            }
        }
    }
}
