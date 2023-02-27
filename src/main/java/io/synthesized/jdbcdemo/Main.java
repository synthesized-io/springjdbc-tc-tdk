package io.synthesized.jdbcdemo;

import io.synthesized.jdbcdemo.db.DbInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication(scanBasePackages = "io.synthesized.jdbcdemo")
public class Main {
    @Autowired
    private DbInit dbInit;

    @PostConstruct
    void init() throws IOException, SQLException {
        dbInit.create();
    }

    public static void main(String[] args) throws SQLException, IOException {
        SpringApplication.run(Main.class, args);
    }
}
