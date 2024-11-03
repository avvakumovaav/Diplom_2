package praktikum.user;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private final String email;
    private final String password;
    private final String name;

    private static Faker faker = new Faker();

    public static User createUser() {
        return new User(faker.internet().emailAddress(),
                faker.internet().password(), faker.name().firstName());
    }

    public static User createUserWithoutPassword() {
        return new User(faker.internet().emailAddress(),
                null, faker.name().firstName());
    }
}
