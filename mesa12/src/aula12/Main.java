package aula12;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;

public class Main {

    private static final Logger logger;
    private static final String createSqlTable = "CREATE TABLE USUARIO (ID INT PRIMARY KEY," +
            " PRIMEIRO_NOME VARCHAR2 NOT NULL," +
            " SOBRENOME VARCHAR2 NOT NULL," +
            " IDADE INT NOT NULL)";
    private static final String dropTable = "DROP TABLE IF EXISTS USUARIO";
    private static final String insertSqlColumn1 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE)" +
            " VALUES(1, 'Lucas', 'Mendonça', 25)";
    private static final String insertSqlColumn2 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE)" +
            " VALUES(2, 'Leandra', 'Ferrari', 21)";
    private static final String insertSqlColumn3 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE)" +
            " VALUES(3, 'Leshly', 'Ontiveros', 18)";
    private static final String deleteSql = "DELETE FROM USUARIO WHERE ID=1";
    private static final String selectSql = "SELECT * FROM USUARIO";

    static {
     logger = Logger.getLogger(Main.class);
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()){

                connection.beginRequest();
                statement.execute(dropTable);
                statement.execute(createSqlTable);
                statement.execute(insertSqlColumn1);
                statement.execute(insertSqlColumn2);
                statement.execute(insertSqlColumn3);
                showUsers(connection);
                statement.execute(deleteSql);
                logger.info("O usuario Lucas foi deletado");
                showUsers(connection);



                connection.commit();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void showUsers(Connection connection) {
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(selectSql);
        ) {
            while (result.next()) {
                logger.warn("Usuario: " + result.getString(2) + " " + result.getString(3));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


        private static Connection getConnection(){
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
