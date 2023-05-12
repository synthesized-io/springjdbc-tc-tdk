package io.synthesized.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.synthesized.tdktc.SynthesizedTDK;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import javax.sql.DataSource;

@TestConfiguration
@ComponentScan({"io.synthesized.jdbcdemo.service"})
public class DaoTestConfiguration {
    private final Network network;
    private final PostgreSQLContainer<?> input;
    private final PostgreSQLContainer<?> output;

    public DaoTestConfiguration() {
        network = Network.newNetwork();
        input = getContainer("input", true);
        output = getContainer("output", false);
        Startables.deepStart(input, output).join();
        new SynthesizedTDK(SynthesizedTDK.DEFAULT_IMAGE_NAME)
                .transform(input, output, """
                        default_config:
                          mode: "GENERATION"
                          target_row_number: 10
                        tables:
                          - table_name_with_schema: "public.talk"
                            transformations:
                              - columns: [ "status" ]
                                params:
                                  type: "categorical_generator"
                                  categories:
                                    type: string
                                    values:
                                      - "NEW"
                                      - "IN_REVIEW"
                                      - "ACCEPTED"
                                      - "REJECTED"
                                  probabilities:
                                    - 0.25
                                    - 0.25
                                    - 0.25
                                    - 0.25
                        global_seed: 42
                          """);
    }


    PostgreSQLContainer<?> getContainer(String name, boolean init) {
        String scriptPath = "io/synthesized/jdbcdemo/dbcreate.sql";
        var result = new PostgreSQLContainer<>("postgres:11.1")
                .withDatabaseName(name)
                .withUsername("user")
                .withPassword("password")
                .withNetwork(network);
        result = init ? result.withInitScript(scriptPath) : result;
        return result;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(output.getJdbcUrl());
        config.setUsername(output.getUsername());
        config.setPassword(output.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
