import java.io.Console;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    public static void main(String[] args) {
        List<String> shoppingCart = new ArrayList<>();
        // List<String> users = new ArrayList<>();
        File db = new File(".\\db");

        if(!db.exists()) {
            db.mkdir();
        }

        if (args.length > 0) {
            System.out.printf("Alternative database path entered as %s.\n",args[0]);
            Path dbPath = Paths.get(args[0]);
            File tempDb = dbPath.toFile();
            // if (!(tempDb == db) && (!tempDb.exists() || tempDb.isFile())) {
            //     tempDb.mkdirs();
            //     db = tempDb;
            //     System.out.printf("Database not found at entered path. Created new dir as %s.\n", db.toString());
            // } else if (tempDb.exists() && tempDb.isFile()) {
            //     db == tempDb;
            // }
            if (tempDb.exists() && !tempDb.isFile()) {
                db = tempDb;
            } else {
                tempDb.mkdirs();
                db = tempDb;
            }
        }

        ShoppingCartDB.setdb(db);
        
        
        Console cons = System.console();
        
        System.out.println("Welcome to your shopping cart");

        boolean flag = true;

        while(flag) {
            String input = cons.readLine(">");

            String[] inputs = input.trim().split(" ", 2);

            // for (String word: inputs) {
            //     System.out.println(word);
            // }

            String action = inputs[0].trim().toLowerCase();

            switch(action) {
                case "list":
                    {
                        if(shoppingCart.size() == 0) {
                            System.out.println("Your cart is empty");
                        } else {
                            for (int i = 0; i < shoppingCart.size(); i++) {
                                System.out.printf("%d. %s\n", (i+1), shoppingCart.get(i));
                            }
                        }
                    }
                    break;
                
                case "add":
                    {
                        String[] items = inputs[1].split(",");
                        for (String item: items) {
                            item = item.trim().toLowerCase();
                            if (!shoppingCart.contains(item)) {
                                shoppingCart.add(item);
                                System.out.printf("%s added to cart\n", item);
                            } else {
                                System.out.printf("You have %s in your cart\n", item);
                            }
                        }
                    }
                    break;

                case "delete":
                    {
                        try {
                            int index = Integer.parseInt(inputs[1]) - 1;
                            if (index < 0 || index >= shoppingCart.size()) {
                                System.out.println("Incorrect item index");
                            } else {
                                String item = shoppingCart.remove(index);
                                System.out.printf("%s removed from cart\n", item);
                            } 
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid input, please add a single whole number after delete.");
                        }
                    }
                    break;
                
                case "quit":
                    {
                        flag = false;
                    }
                    break;

                case "login":
                    {
                        String user = inputs[1];
                        shoppingCart = ShoppingCartDB.load(user);
                    }
                    break;

                case "save":
                    {
                        ShoppingCartDB.save(shoppingCart);
                    }
                    break;
                
                case "users":
                    {
                        ShoppingCartDB.users();
                    }
                    break;

                default:
                    System.out.println("""
                            Please use the following commands:
                            - list items in current list (list)
                            - add items to current (add item1, item2, ...)
                            - delete an item by item number (delete itemNumber)
                            - login as a user and reads the shopping cart in their file (login username)
                                - opens new file if new user
                                - opens existing file if existing user
                                - replaces current shopping cart with user's shopping cart
                            - save current items in shopping cart to user's file (save)
                                -Must be logged in as a user to work; else a warning appears nothing happens
                            - users which are in the current database directory are listed (users)
                            - quit the program (quit)""");
                    break;
            }

            
        }

    }
}
