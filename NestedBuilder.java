package interview;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NestedBuilder {
  public static void main(String[] args) {
    Car car = Car.Builder.create()
      .withModel("CX-5")
      .withManufacturer()
        .withName("Mazda")
        .withAddress()
          .withCity("Tokyo")
          .withStreet("Akagashi Street")
          .end()
        .end()
      .withFeature()
        .withName("Auto A/C")
        .withCost(123)
        .end()
      .withFeature()
        .withName("Heated front seats")
        .withCost(245)
        .end()
      .build();

    System.out.println(car);
  }
}

// Data classes
// Car
//  - model, String: CX-5
//  - manifacturer: Manufacturer
//      -- name, String: Mazda
//      -- address, Address
//          -- City, String
//          -- Street, String
// -- List<Feature>
//    -- Feature:
//      -- name
//      -- cost

class Car {
  private String model;
  private Manufacturer manufacturer;
  private List<Feature> fetures;

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }

  public List<Feature> getFetures() {
    return fetures;
  }

  public void setFetures(List<Feature> fetures) {
    this.fetures = fetures;
  }

  @Override
  public String toString() {
    return "Car[Model: " + model + ", " + manufacturer + " Features:[" + fetures.stream().map(f -> f.toString()).collect(Collectors.joining(", ")) + "]]";
  }

  static class Builder {
    private Car car;

    private Builder(Car car) {
      this.car = car;
    }

    static Builder create() {
      return new Builder(new Car());
    }

    Builder withModel(String model) {
      this.car.setModel(model);
      return this;
    }

    Manufacturer.Builder withManufacturer() {
      return Manufacturer.Builder.create(this);
    }

    Feature.Builder withFeature() {
      if (car.getFetures() == null) {
        car.setFetures(new ArrayList<>());  
      }
      
      return Feature.Builder.create(this);
    }

    Car build() {
      return car;
    }    
  }
}

class Manufacturer {
  private String name;
  private Address address;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Manufacturer[Name: " + name + ", " + address.toString() + "]";    
  }

  static class Builder {
    private Manufacturer manufacturer;
    private Car.Builder carBuilder;

    private Builder(Car.Builder carBuilder, Manufacturer manufacturer) {
      this.carBuilder = carBuilder;
      this.manufacturer = manufacturer;

      // this.carBuilder.build().setManufacturer(manufacturer);
    }

    static Builder create(Car.Builder carBuilder) {
      return new Builder(carBuilder, new Manufacturer());
    }

    Builder withName(String name) {
      this.manufacturer.setName(name);

      return this;
    }

    Address.Builder withAddress() {
      return Address.Builder.create(this);
    }

    Car.Builder end() {
      this.carBuilder.build().setManufacturer(manufacturer);
      return carBuilder;
    }

    Manufacturer build() {
      return manufacturer;
    }
  }
}

class Address {
  private String city;
  private String street;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String toString() {
    return "Address[City: " + city + ", Street: " + street + "]"; 
  }

  static class Builder {
    private Address address;
    private Manufacturer.Builder manufacturerBuilder;

    private Builder(Manufacturer.Builder manufacturerBuilder, Address address) {
      this.manufacturerBuilder = manufacturerBuilder;
      this.address = address;
    }

    static Address.Builder create(Manufacturer.Builder manufacturerBuilder) {
      return new Builder(manufacturerBuilder, new Address());
    }

    Builder withCity(String city) {
      address.setCity(city);

      return this;
    }

    Builder withStreet(String street) {
      address.setStreet(street);

      return this;
    }

    Manufacturer.Builder end() {
      manufacturerBuilder.build().setAddress(address);
      return manufacturerBuilder;
    }
  }
}

class Feature {
  private String name;
  private Integer cost;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  @Override
  public String toString() {
    return "Feature[Name: " + name + ", Cost: " + cost + "]";
  }

  static class Builder {
    private Car.Builder carBuilder;
    private Feature feature;

    private Builder(Car.Builder carBuilder, Feature feature) {
      this.carBuilder = carBuilder;
      this.feature = feature;
    }

    static Builder create(Car.Builder carBuilder) {
      return new Builder(carBuilder, new Feature());
    }

    Builder withName(String name) {
      feature.setName(name);

      return this;
    }

    Builder withCost(Integer cost) {
      feature.setCost(cost);

      return this;
    }

    Car.Builder end() {
      carBuilder.build().getFetures().add(feature);
      return carBuilder;
    }
  }
}
