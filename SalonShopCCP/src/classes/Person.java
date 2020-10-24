package classes;

public abstract class Person {
  private String name;

  public Person(String new_name) {
    this.name = new_name;
  }
  
  public String getName() {
    return this.name;
  }
}
