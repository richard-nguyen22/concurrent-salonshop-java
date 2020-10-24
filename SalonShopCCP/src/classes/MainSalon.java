package classes;
import java.util.concurrent.*;

public class MainSalon {
  static int runTime = 45000; // Runtime of Salon Shop System, in milliseconds
  
  public static void main(String[] args) {
    System.out.println("\t== WELCOME TO MODERN SALON ==");
    System.out.println(String.format("The salon is open for %d seconds", 
      runTime/1000));
    // Create a thread pool with maximum 6 threads
    ExecutorService executor = Executors.newFixedThreadPool(6);
    
    // Create shared object shop
    SalonShop shop = new SalonShop();
    // Use executor to start all threads
    executor.execute(new Barber(1, shop));
    executor.execute(new Barber(2, shop));
    executor.execute(new Barber(3, shop));
    executor.execute(new BarberSet("Barber set #1", shop));
    executor.execute(new BarberSet("Barber set #2", shop));
    executor.execute(new NewCustomer(shop));
    
    try {
      // Set the runtime of the Salon shop
      Thread.sleep(runTime);
    } catch (InterruptedException ex) { }
    shop.closeShop(); // Announce close the shop
    
    // Wait until the last customer is served
    while (!shop.getSitting().isEmpty()) {}
    shop.printTotalCustomers(); // Print total customers
    
    // Shut down the executor to stop the program naturally
    executor.shutdown();
  }
}
