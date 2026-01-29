package MainUtilities.Miscellaneous;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RandomDataGeneratorUtil {

    private static Faker faker = new Faker(new Locale("en-IND"));

    // Random First Name
    public static String getFirstName() {
        return faker.name().firstName();
    }

    // Random Last Name
    public static String getLastName() {
        return faker.name().lastName();
    }

    // Random Full Name
    public static String getFullName() {
        return faker.name().fullName();
    }

    // Random Email
    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    // Random Phone Number
    public static String getPhoneNumber() {
        return faker.phoneNumber().cellPhone().replaceAll("[^0-9]", "");
    }

    // Random Username
    public static String getUsername() {
        return faker.name().username();
    }

    // Random Password
    public static String getPassword() {
        return faker.internet().password(15, 25, true, true, true);
    }

    // Random Number in Range
    public static int getRandomNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    // Random Address
    public static String getAddress() {
        return faker.address().fullAddress();
    }

    // Random Company Name
    public static String getCompanyName() {
        return faker.company().name();
    }

    // Random Date of Birth
    public static String getDateOfBirth() {
        Date dob = faker.date().birthday();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(dob);
    }

    // Unique ID
    public static String getUUID() {
        return faker.idNumber().valid();
    }

    // -------- Main method to test utility --------
    public static void main(String[] args) {

        System.out.println("First Name: " + getFirstName());
        System.out.println("Last Name: " + getLastName());
        System.out.println("Full Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhoneNumber());
        System.out.println("Username: " + getUsername());
        System.out.println("Password: " + getPassword());
        System.out.println("Random Number (1-100): " + getRandomNumber(1, 100));
        System.out.println("Address: " + getAddress());
        System.out.println("Company: " + getCompanyName());
        System.out.println("DOB: " + getDateOfBirth());
        System.out.println("UUID: " + getUUID());
    }
}

