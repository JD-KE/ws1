import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class ws1 {
    public static void main(String[] args) {
        List<String> shoppingCart = new ArrayList<>();
        
        Console cons = System.console();
        
        System.out.println("Welcome to your shopping cart");

        boolean flag = true;

        while(flag) {
            String input = cons.readLine();

            String[] inputs = input.trim().split(" ", 2);

            for (String word: inputs) {
                System.out.println(word);
            }

            String action = inputs[0];

            switch(action) {
                case "list":
                    {
                        // if(shoppingCart.size() == 0) {
                        //     System.out.println("Your cart is empty");
                        // } else {
                        //     for (int i; i < shoppingCart.size(); i++) {
                        //         System.out.printf("%d. %s\n", (i+1), shoppingCart.get(i));
                        //     }
                        // }
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
                default:
                    System.out.println("Nothing happened");
                    break;
            }

            flag = false;
        }

    }
}