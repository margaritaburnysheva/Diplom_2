package user;

import com.github.javafaker.Faker;

public class UserDataGeneratorWithNull {
    public static User getDataWithoutName(){
        Faker faker=new Faker();
        String email=faker.internet().emailAddress();
        String password=faker.internet().password(6,10);
        return new User(null,email,password);
    }
    public static User getDataWithoutEmail(){
        Faker faker=new Faker();
        String name=faker.name().name();
        String password=faker.internet().password(6,10);
        return new User(name,null,password);
    }
    public static User getDataWithoutPassword(){
        Faker faker=new Faker();
        String name=faker.name().name();
        String email=faker.internet().emailAddress();
        return new User(name,email,null);
    }
}
