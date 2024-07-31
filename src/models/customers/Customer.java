package models.customers;

import constants.Constants;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
  private final String firstName;
  private final String lastName;
  private final String email;

  public Customer(String email, String firstName, String lastName) {
    this.firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
    this.lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
    var re = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    if (!re.matcher(email).matches()) {
      throw new IllegalArgumentException("Invalid email format");
    }
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Customer c) {
      return email.equals(c.getEmail());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(email);
  }

  @Override
  public String toString() {
    return String.format("Customer information: Full name: %s; Email: %s", getFullName(), email);
  }
}
