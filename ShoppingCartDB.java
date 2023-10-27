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
        try {
            if (userFile.createNewFile()) {
                FileWriter writer = new FileWriter(userFile);
                writer.write(userFile.toString());
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("An error occurred when logging in.");
            // e.printStackTrace();
        }

        // reads user file for items and returns list to
        try {
            Scanner sc = new Scanner(userFile);
           
            while(sc.hasNextLine()) {              
                shoppingCart.add(sc.nextLine());
            }
            sc.close();

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
            System.err.println("An error occurred.");
            // e.printStackTrace();
        }

        return shoppingCart;
    }

    public static void save (List<String> shoppingCart) {
        //checks if there is a user logged in; if so, writes cart into user file
        if (currentUser == null) {
            System.out.println("Please log in as a user before saving.");
        } else {
            try {
                File userFile = new File(db, String.format("%s.db",currentUser));
                FileWriter writer = new FileWriter(userFile, false);

                writer.write(String.format("%s%n",userFile.toString()));

                for (String item : shoppingCart) {
                    writer.write(String.format("%s%n", item));
                }
                writer.close();

                System.out.println("Your cart has been saved.");

                currentUser = null;
                shoppingCart.clear();

            } catch (IOException e) {
                System.err.println("An error occurred when logging in.");
                // e.printStackTrace();
            }
        }
    }

    public static void users() {
        System.out.println("The following users are registered");
        File [] files = db.listFiles();

        for (int i = 0; i < files.length; i++) {
            String extName = files[i].getName();
            if (extName.endsWith(".db")) {
                System.out.printf("%d. %s%n", (i+1), extName.substring(0, (extName.length() - 3)));
            }
        }
    }
}
