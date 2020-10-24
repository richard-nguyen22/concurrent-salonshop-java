package classes;

public class Barber extends Person implements Runnable {
  private final SalonShop shop;
  private double money;
  private int customer_count;
  private final String salon_chair;
 
  public Barber(int id, SalonShop shop) {
    super("Barber #" + id);
    this.money = 0.0;
    this.customer_count = 0;
    this.shop = shop;
    this.salon_chair = "Salon chair #" + id;
  }
  
  private void announceStart() {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " starts working"); break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " starts working"); break;
      default:
        System.out.println("\t\t\t" + this.getName() + " starts working");
    }
  }
  
  private void announceCheckCustomer() {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " checks customers in sitting area"); break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " checks customers in sitting area"); break;
      default:
        System.out.println("\t\t\t" + this.getName() + " checks customers in sitting area"); break;
    }
  }
  
  private void announceSleep() {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\tNo customer. " + this.getName() + " sleeps"); break;
      case "Barber #2":
        System.out.println("\t\tNo customer. " + this.getName() + " sleeps"); break;
      default:
        System.out.println("\t\t\tNo customer. " + this.getName() + " sleeps");
    }
  }

  public void announceServe(Customer customer) {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " sees and serves " + customer.getName()); break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " sees and serves " + customer.getName()); break;
      default:
        System.out.println("\t\t\t" + this.getName() + " sees and serves " + customer.getName());
    }
  }

  public String getSalonChair() {
    return this.salon_chair;
  }
  
  public void announceWait(Customer customer) {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " waits for a barber set. ");
        break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " waits for a barber set. ");
        break;
      default:
        System.out.println("\t\t\t" + this.getName() + " waits for a barber set. ");
    }
  }
  
  public void announceTake(BarberSet set) {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " takes " + set.getLabel());
        break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " takes " + set.getLabel());
        break;
      default:
        System.out.println("\t\t\t" + this.getName() + " takes " + set.getLabel());
    }
  }
  
  public void announceCut(Customer customer, int cut_time) {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t"+this.getName() + " cuts hair for " + 
          customer.getName()+" in "+cut_time/1000+" seconds");
        break;
      case "Barber #2":
        System.out.println("\t\t"+this.getName() + " cuts hair for " + 
          customer.getName()+" in "+cut_time/1000+" seconds");
        break;
      default:
        System.out.println("\t\t\t"+this.getName() + " cuts hair for " + 
          customer.getName()+" in "+cut_time/1000+" seconds");
    }
  }
  
  public void freeBarberSet(BarberSet set) {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t"+this.getName() + " finishes cutting hair and puts " + set.getLabel() + " on the table");
        break;
      case "Barber #2":
        System.out.println("\t\t"+this.getName() + " finishes cutting hair and puts " + set.getLabel() + " on the table");
        break;
      default:
        System.out.println("\t\t\t"+this.getName() + " finishes cutting hair and puts " + set.getLabel() + " on the table");
    }
  }
  
  public void getPaid(double amount) {
    this.money += amount;
    this.customer_count++;
  }

  private void announceClose() {
    switch (this.getName()) {
      case "Barber #1":
        System.out.println("\t"+this.getName() + " stops working and closes the shop");
        break;
      case "Barber #2":
        System.out.println("\t\t"+this.getName() + " stops working and closes the shop");
        break;
      default:
        System.out.println("\t\t\t"+this.getName() + " stops working and closes the shop");
    }
  }

  private void announceEarning() {
    System.out.println("EARNING: " + this.getName() + " cut hair for " + 
      this.customer_count + " customers and earn RM" + this.money);
  }
  
  @Override
  public void run() {
    while (this.shop.getSets().size() != 2) {      
      this.shop.waitForSetToReady(); // wait until two barber sets are created
    }
    this.announceStart();
    // Keep working until the shop is closed or there is no customer in sitting chairs
    while (!this.shop.isClose()) {
      // The barber begins to check customers
      this.announceCheckCustomer();
      if (this.shop.getSitting().isEmpty()) {
        // No customer, barber will sleep
        this.shop.increaseSleep(); // Increase sleep_count concurrently
        this.announceSleep();        
        this.shop.letBarberSleep(); // wait() is called in letBarberSleep
        this.shop.decreaseSleep(); // Barber is waked up, decrease sleep_count concurrently
      }
      if (!this.shop.isClose()) {
        // Barber gets new customer from sitting area to serve
        Customer serving_customer = this.shop.getNewCustomer(this);
        this.shop.cutHair(this, serving_customer); 
      } 
      // Finish cutting hair for a customer, repeat the loop
    }
    // The shop is close but there are waiting customers, serve them all before closing
    while (!this.shop.getSitting().isEmpty()) {
      Customer serving_customer = this.shop.getNewCustomer(this);
      this.shop.cutHair(this, serving_customer); 
    }
    this.announceClose();
    this.announceEarning();
  }
}
























