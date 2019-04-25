package com.csl456.bikerentalapp.db;

import com.csl456.bikerentalapp.core.*;
import io.dropwizard.testing.junit5.*;
import org.hibernate.exception.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CycleDAOTest {

    private final DAOTestExtension daoTestRule = DAOTestExtension
            .newBuilder()
            .addEntityClass(Cycle.class)
            .build();

    private CycleDAO cycleDAO;

    @Test
    public void createCycle() {
        final Cycle cycle
                = daoTestRule.inTransaction(() -> cycleDAO.create(new Cycle("Atlas",
                1,
                1,
                1
        )));
        assertThat(cycle.getId()).isGreaterThan(0);
        assertThat(cycle.getBrand()).isEqualTo("Atlas");
        assertThat(cycle.getLocationId()).isEqualTo(1);
        assertThat(cycle.getOwnerId()).isEqualTo(1);
        assertThat(cycleDAO.findById(cycle.getId())).isEqualTo(cycle);
    }

    @Test
    public void findAll() {
        daoTestRule.inTransaction(() -> {
            cycleDAO.create(new Cycle("Atlas", 1, 1, 1));
            cycleDAO.create(new Cycle("Avon", 2, 2, 1));
        });
        final List<Cycle> cycles = cycleDAO.findAll();
        assertThat(cycles).extracting("brand").containsOnly("Atlas", "Avon");
        assertThat(cycles).extracting("locationId").containsOnly(1, 2);
        assertThat(cycles).extracting("ownerId").containsOnly(1, 2);
    }

    @Test
    public void handlesNullOwner() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> daoTestRule.inTransaction(() -> cycleDAO.create(new Cycle(
                        null,
                        1,
                        1,
                        1
                ))));
    }

    //TODO remove cycle

    @BeforeEach
    public void setUp() {
        cycleDAO = new CycleDAO(daoTestRule.getSessionFactory());
    }

}
