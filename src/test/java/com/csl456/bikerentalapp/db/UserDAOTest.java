package com.csl456.bikerentalapp.db;

import com.csl456.bikerentalapp.core.*;
import io.dropwizard.testing.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import javax.persistence.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(DropwizardExtensionsSupport.class)
public class UserDAOTest {

    private final DAOTestExtension daoTestRule = DAOTestExtension
            .newBuilder()
            .addEntityClass(User.class)
            .build();

    private UserDAO userDAO;

    @Test
    public void createUser() {
        final User user
                = daoTestRule.inTransaction(() -> userDAO.create(new User(
                "Vinit",
                "abc",
                UserRole.ADMIN,
                1
        )));
        assertThat(user.getUsername()).isEqualTo("Vinit");
        assertThat(user.getPassword()).isEqualTo("abc");
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(user.getPersonId()).isEqualTo(1);
        assertThat(userDAO.findUsersByUsernameAndPassword("Vinit",
                "abc"
        )).isEqualTo(user);
    }

    @Test
    public void handlesNullFields() {
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> daoTestRule
                .inTransaction(() -> userDAO.create(new User(null,
                        "abc",
                        UserRole.ADMIN,
                        1
                ))));
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> daoTestRule
                .inTransaction(() -> userDAO.create(new User("Vinit",
                        null,
                        UserRole.ADMIN,
                        1
                ))));
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> daoTestRule
                .inTransaction(() -> userDAO.create(new User("Vinit",
                        "abc",
                        null,
                        1
                ))));
    }

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO(daoTestRule.getSessionFactory());
    }

}
