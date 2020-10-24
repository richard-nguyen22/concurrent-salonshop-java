package classes;

import java.util.Random;

/** This class generates new customers for Salon shop. 
 */
public class NewCustomer extends Thread {
  private final SalonShop shop;
  private int customer_id; // The id customers come to the shop
  private final Random rand = new Random();
  
  NewCustomer(SalonShop shop) {
    this.shop = shop;
    this.customer_id = 1;
  }

  @Override
  public void run() {
    while (this.shop.getSets().size() != 2) {      
      this.shop.waitForSetToReady(); // wait until two barber sets are created
    }
    // Creating new customers until the shop is closed
    while(!shop.isClose()) {
      try {
        Customer new_customer = new Customer("Customer #" + customer_id, shop);
        customer_id++;
        // Create new customers
        Thread customer_thread = new Thread(new_customer);
        customer_thread.start();
        // A new customer comes after a random time
        int next_time = (rand.nextInt(1) + 1)*1000; // rand.nextInt(0): random 0 to 1, (rand.nextInt(0) + 1): random 1 to 2,
        Thread.sleep(next_time);
      } catch(InterruptedException e) { }
    }
  }
}
