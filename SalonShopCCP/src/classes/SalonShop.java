package classes;
import java.util.Random;
import java.util.concurrent.*;

public class SalonShop {
  private static ArrayBlockingQueue<Customer> sitting;
  private static ArrayBlockingQueue<Customer> standing;
  private static ArrayBlockingQueue<BarberSet> sets;
  private boolean close;
  private final Random rand = new Random();
  private int sleep_count;   // sleep_count > 0, which mean there are sleeping barbers
  private int total_customers;
  private int leave_customers;

  SalonShop() {
    // true means fairness is true
    sitting = new ArrayBlockingQueue<>(10, true); // Array for 10 sitting customers
    standing = new ArrayBlockingQueue<>(10, true); // Array for 10 standing customers
    sets = new ArrayBlockingQueue<>(2, true);  // Array for 2 barber sets
    close = false;
    sleep_count = 0;
    total_customers = 0;
  }

  public ArrayBlockingQueue<Customer> getSitting() {
    return SalonShop.sitting;
  }
  
  public ArrayBlockingQueue<BarberSet> getSets() {
    return SalonShop.sets;
  }

  public synchronized void waitForSetToReady() {
    try {
      wait();  // Barber waits for all barber sets to be ready
    } catch (InterruptedException ex) { }
  }

  public synchronized void setIsReady() {
    notifyAll(); // notify Barber threads and NewCustomer thread continue to run
  }

  private synchronized void wakeBarberUp(Customer new_customer) {
    System.out.println(new_customer.getName() + " wakes a barber up");
    notify();
  }
  
  public void addNewCustomer(Customer new_customer) {
    if (sleep_count > 0) {
      // There are sleeping barbers, new_customer wakes any sleeping barber up
      this.wakeBarberUp(new_customer);  // notify() is used to wake a barber up
      sitting.add(new_customer);
    } else if (sitting.size() < 10) {
      // new_customer sits if there is an empty chair
      new_customer.sit();
      sitting.add(new_customer);
    } else if (standing.size() < 10) {
      // new_customer stands if there is no empty chair
      standing.add(new_customer);      
      new_customer.stand();
    } else if (sitting.size() == 10 && standing.size() == 10) {
      // The shop if full: There are 10 sitting customers and 10 standing
      // customers, new_customer leaves
      new_customer.leave();
      leave_customers += 1;
    }
  }

  public synchronized void letBarberSleep() {
    try {
      wait(); // the barber waits for a new customer to wake him up
    } catch (InterruptedException e) {}
  }
  
  public Customer getNewCustomer(Barber barber) {
    // Barber gets a new customer from sitting area
    Customer serving_customer = null;
    try {
      serving_customer = SalonShop.sitting.take();
    } catch (InterruptedException e) {}      
    barber.announceServe(serving_customer);
    serving_customer.sitOnSalonChair(barber); // New customer sits on barber's salon chair
    this.increaseTotalCustomers(); // Increase total served customers concurrently
    try {
      // The longest standing customer sit on the available chair
      if (!SalonShop.standing.isEmpty()) {
        Customer moving_customer = SalonShop.standing.take();
        moving_customer.standToSit();
        SalonShop.sitting.add(moving_customer);
      }      
    } catch (InterruptedException e) { }
    return serving_customer;
  }
  
  private synchronized void waitForSet(Barber barber, Customer customer) {
    try {
      wait();
    } catch (InterruptedException e) {}
  }

  public void cutHair(Barber barber, Customer serving_customer) {
    // No barber set is availble, barber waits for a barber set
    while (this.getSets().isEmpty()) {
      barber.announceWait(serving_customer); 
      this.waitForSet(barber, serving_customer); // wait() is called in waitForSet
    }
    try {
      // Barber use an available barber set from sets to cut hair
      BarberSet used_set = this.getSets().take(); 
      int cut_time = (rand.nextInt(3) + 3)*1000; // cut hair time: 4 - 7 seconds
      // The customer chooses hairstyle
      barber.announceTake(used_set);
      serving_customer.chooseHairStyle(barber);
      barber.announceCut(serving_customer, cut_time);
      Thread.sleep(cut_time);
      // Finish cutting, put the barber set back to sets
      this.getSets().add(used_set);
      barber.freeBarberSet(used_set);
    } catch (InterruptedException e) { }
    if (this.sleep_count == 0) {
      notifyBarberSet(); // Only notify if there is no sleeping barber
    }
    serving_customer.pay(barber);
  }
  
  private synchronized void notifyBarberSet() {
    notify();
  }
  
  public boolean isClose() {
    return this.close;
  }

  public synchronized void closeShop() {
    this.close = true;
    if (SalonShop.sitting.isEmpty()) 
      System.out.println("The shop is close.");
    else {
      System.out.println("####\nThe shop is going to close. No new customers come.");
      System.out.println("There are still " + standing.size()
        + " standing customers and " + sitting.size() + " sitting customers.");
      System.out.println("Barbers serve all waiting customers in the shop");
    }
    notifyAll(); // Notify sleeping barbers to close the shop
  }
  
  public void printTotalCustomers() {
    System.out.println("\n####\nTotal number of served customers: " + this.total_customers);
    System.out.println("Total number of left customers: " + this.leave_customers);
  }
  
  private synchronized void increaseTotalCustomers() {
    this.total_customers++;
  }

  public synchronized void increaseSleep() {
    this.sleep_count++;
  }
  
  public synchronized void decreaseSleep() {
    this.sleep_count--;
  }
  
//  public void printSleepCount() {
//    System.out.println("Sleep count: " + this.sleep_count);
//  }
//  
}
