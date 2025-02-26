package pt.ridenexus.vehicle.containers;

import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import pt.ridenexus.vehicle.persistence.rdb.VehicleRepository;

//suppressed because it is a false positive - https://stackoverflow.com/a/75454305/2966971
@Getter(AccessLevel.PROTECTED)
@SuppressWarnings("resource")
@Tag("IntegrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Autowired
    VehicleRepository repo;

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
}
