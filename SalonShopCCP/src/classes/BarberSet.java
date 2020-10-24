package classes;

public class BarberSet extends Thread {
  private final String label;
  private final SalonShop shop;

  public BarberSet(String label, SalonShop shop) {
    this.label = label;
    this.shop = shop;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  @Override
  public void run() {
    System.out.println(this.label + " is ready to use");
    shop.getSets().add(this);
    if (shop.getSets().size() == 2)
      shop.setIsReady();
  }  
}
