package database;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static database.Constants.Rights.RIGHTS;
import static database.Constants.Roles.*;
import static database.Constants.Schemas.SCHEMAS;
import static database.Constants.getRolesRights;

// Script - code that automates some steps or processes

public class Bootstrap {

    private static RightsRolesRepository rightsRolesRepository;

    public static void main(String[] args) throws SQLException {
        dropAll();

        bootstrapTables();

        bootstrapUserData();
    }

    private static void dropAll() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Dropping all tables in schema: " + schema);

            Connection connection = new JDBConnectionWrapper(schema).getConnection();
            Statement statement = connection.createStatement();

            String[] dropStatements = {
                    "TRUNCATE `" + schema + "`.`role_right`;",
                    "DROP TABLE `" + schema + "`.`role_right`;",
                    "TRUNCATE `" + schema + "`.`right`;",
                    "DROP TABLE `" + schema + "`.`right`;",
                    "TRUNCATE `" + schema + "`.`user_role`;",
                    "DROP TABLE `" + schema + "`.`user_role`;",
                    "TRUNCATE `" + schema + "`.`role`;",
                    "DROP TABLE `" + schema + "`.`book`, `" + schema + "`.`role`, `" + schema + "`.`user`, `" + schema + "`.`orders`;"
            };

            Arrays.stream(dropStatements).forEach(dropStatement -> {
                try {
                    statement.execute(dropStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Done table bootstrap");
    }

    private static void bootstrapTables() throws SQLException {
        SQLTableCreationFactory sqlTableCreationFactory = new SQLTableCreationFactory();

        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping " + schema + " schema");


            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            Connection connection = connectionWrapper.getConnection();

            Statement statement = connection.createStatement();

            for (String table : Constants.Tables.ORDERED_TABLES_FOR_CREATION) {
                String createTableSQL = sqlTableCreationFactory.getCreateSQLForTable(table);
                statement.execute(createTableSQL);
            }
        }
        System.out.println("Done table bootstrap");
    }

    private static void bootstrapUserData() throws SQLException {
        for (String schema : SCHEMAS) {
            System.out.println("Bootstrapping user data for " + schema);

            JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper(schema);
            rightsRolesRepository = new RightsRolesRepositoryMySQL(connectionWrapper.getConnection());

            bootstrapRoles();
            bootstrapRights();
            bootstrapRoleRight();
            populateTables(connectionWrapper.getConnection());
            bootstrapUserRoles(connectionWrapper.getConnection());
        }

    }

    private static void bootstrapRoles() {
        for (String role : ROLES) {
            rightsRolesRepository.addRole(role);
        }
    }

    private static void bootstrapRights() {
        for (String right : RIGHTS) {
            rightsRolesRepository.addRight(right);
        }
    }

    private static void bootstrapRoleRight() {
        Map<String, List<String>> rolesRights = getRolesRights();

        for (String role : rolesRights.keySet()) {
            Long roleId = rightsRolesRepository.findRoleByTitle(role).getId();

            for (String right : rolesRights.get(role)) {
                Long rightId = rightsRolesRepository.findRightByTitle(right).getId();

                rightsRolesRepository.addRoleRight(roleId, rightId);
            }
        }
    }

    private static void bootstrapUserRoles(Connection connection) {
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        Role adminRole = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        User user = new UserBuilder()
                .setUsername("root@root.com")
                .setPassword(authenticationService.hashPassword("root123!"))
                .setRoles(Collections.singletonList(adminRole))
                .build();
        userRepository.save(user);

        Role employeeRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        User user1 = new UserBuilder()
                .setUsername("emp@emp.com")
                .setPassword(authenticationService.hashPassword("root123!"))
                .setRoles(Collections.singletonList(employeeRole))
                .build();
        userRepository.save(user1);

        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER);
        User user2 = new UserBuilder()
                .setUsername("cust@cust.com")
                .setPassword(authenticationService.hashPassword("root123!"))
                .setRoles(Collections.singletonList(customerRole))
                .build();
        userRepository.save(user2);
    }

    private static void populateTables(Connection connection) throws SQLException {
        SQLTablePopulationFactory sqlTablePopulationFactory = new SQLTablePopulationFactory();

        String populateTableSQL = sqlTablePopulationFactory.getPopulateSQLForTableBook();

        Statement statement = connection.createStatement();
        statement.execute(populateTableSQL);
    }
}