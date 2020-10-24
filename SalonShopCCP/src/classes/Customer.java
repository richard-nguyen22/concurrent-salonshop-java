package classes;

import java.util.Random;

public class Customer extends Person implements Runnable {
  private double money;
  private final SalonShop shop;

  public Customer(String new_name, SalonShop shop) {
    super(new_name);
    this.money = 15;
    this.shop = shop;
  }
  
  public void sit() {
    System.out.println(this.getName() + " sits on a waiting chair");
  }

  public void stand() {
    System.out.println(this.getName() + " stands in waiting area");
  }

  public void standToSit() {
    System.out.println(this.getName() + " sees an empty waiting chair and sits on it");
  }
  
  public void sitOnSalonChair(Barber barber) {
    switch (barber.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " sits on " + barber.getSalonChair()); 
        break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " sits on " + barber.getSalonChair());
        break;
      default:
        System.out.println("\t\t\t" + this.getName() + " sits on " + barber.getSalonChair()); 
    }
  }
  
  public void chooseHairStyle(Barber barber) {
    String style = null;
    Random random_style = new Random();    
    switch (random_style.nextInt(2)) {
      case 0:        
        style = "Undercut hair style";
        break;
      case 1:
        style = "Shave-all style";
        break;
      case 2:
        style =  "Prisoner hair style";
        break;
    }
    switch (barber.getName()) {
      case "Barber #1":
        System.out.println("\t" + this.getName() + " chooses " + style); 
        break;
      case "Barber #2":
        System.out.println("\t\t" + this.getName() + " chooses " + style); 
        break;
      default:
        System.out.println("\t\t\t" + this.getName() + " chooses " + style); 
    }
  }
  
  public void leave() {
    System.out.println(this.getName() + " leaves because the shop is full");
  }

  public void pay(Barber barber) {
    switch (barber.getName()) {
      case "Barber #1":
        System.out.println("\t"+this.getName() + " pays RM15 to " + barber.getName() + " and leaves");
        break;
      case "Barber #2":
        System.out.println("\t\t"+this.getName() + " pays RM15 to " + barber.getName() + " and leaves");
        break;
      default:
        System.out.println("\t\t\t"+this.getName() + " pays RM15 to " + barber.getName() + " and leaves");
    }
    barber.getPaid(this.money);
    this.money = 0.0;
  }
  
  @Override
  public void run() {
    System.out.println(this.getName() + " comes to salon shop");
    this.shop.addNewCustomer(this);
  }
}
