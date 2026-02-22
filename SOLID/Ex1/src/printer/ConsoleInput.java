package printer;

import java.util.Scanner;

// Deliberately unused right now (smell: leftover artifact).
public class ConsoleInput {
   private final Scanner scanner = new Scanner(System.in);

   public String readLine(){
       return scanner.nextLine();
   }

   public void close(){
       scanner.close();
   }

}
