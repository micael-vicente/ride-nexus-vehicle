package pt.ridenexus.vehicle.it.containers;

import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import pt.ridenexus.vehicle.persistence.rdb.JpaVehicleRepository;

import java.util.Set;

//suppressed because it is a false positive - https://stackoverflow.com/a/75454305/2966971
@Getter(AccessLevel.PROTECTED)
@SuppressWarnings("resource")
@Tag("IntegrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Autowired
    HttpGraphQlTester graphQlTester;

    @Autowired
    JpaVehicleRepository repo;

    @BeforeEach
    void cleanUp() {
        repo.deleteAll();
    }

    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.2"))
        .withDatabaseName("postgres")
        .withCopyFileToContainer(MountableFile.forClasspathResource("postgres/init.sql"), "/docker-entrypoint-initdb.d/")
        .withUsername("postgres")
        .withPassword("postgres");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    protected boolean containsAll(String toTest, Set<String> tokens) {
        for(String token : tokens) {
            if(!toTest.contains(token)) {
                return false;
            }
        }
        return true;
    }
}
