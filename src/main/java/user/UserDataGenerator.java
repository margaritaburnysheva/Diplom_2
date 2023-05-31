package user;

import com.github.javafaker.Faker;

public class UserDataGenerator {
    static Faker faker=new Faker();
    public static String randomName=faker.name().name();
    public static String secondRandomName=faker.name().name();
    public  static String randomEmail=faker.internet().emailAddress();
    public  static String secondRandomEmail=faker.internet().emailAddress();
    public static String randomPassword=faker.internet().password(6,10);
    public static String secondRandomPassword=faker.internet().password(6,10);
}
