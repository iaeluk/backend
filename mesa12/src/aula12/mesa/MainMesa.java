package aula12.mesa;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;

public class MainMesa {

    private static final Logger logger;
    private static final String createSqlTable = "CREATE TABLE USUARIO (ID INT PRIMARY KEY, PRIMEIRO_NOME VARCHAR2 NOT NULL, SOBRENOME VARCHAR2 NOT NULL, IDADE INT NOT NULL, EMAIL VARCHAR2 NOT NULL)";
    private static final String dropTable = "DROP TABLE IF EXISTS USUARIO";
    private static final String insertSqlColumn1 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE, EMAIL)" +
            " VALUES(1, 'Lucas', 'Mendonça', 25, 'lucas@dh.com')";
    private static final String insertSqlColumn2 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE, EMAIL)" +
            " VALUES(2, 'Lucas', 'Mendonça', 25, 'lucas@dh.com')";
    private static final String insertSqlColumn3 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE, EMAIL)" +
            " VALUES(3, 'Leandra', 'Ferrari', 18, 'leandra@dh.com')";
    private static final String insertSqlColumn4 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE, EMAIL)" +
            " VALUES(3, 'Leshly', 'Ontiveros', 17, 'less@dh.com')";
    private static final String insertSqlColumn5 = "INSERT INTO USUARIO(ID, PRIMEIRO_NOME, SOBRENOME, IDADE, EMAIL)" +
            " VALUES(4, 'Maria', 'Pereira', 34, 'maria@dh.com')";
    private static final String updateSql = "UPDATE USUARIO SET IDADE = 30 WHERE SOBRENOME = 'Ferrari'";
    private static final String deleteSql = "DELETE FROM USUARIO WHERE ID=1";
    private static final String selectSql = "SELECT * FROM USUARIO";

    static {
        logger = Logger.getLogger(MainMesa.class);
    }


    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        try(Connection connection = getConnection(); Statement statement = connection.createStatement()) {

            statement.execute(dropTable);
            statement.execute(createSqlTable);
            statement.execute(insertSqlColumn1);
            statement.execute(insertSqlColumn2);
            statement.execute(insertSqlColumn3);

            try {
                statement.execute(insertSqlColumn4);
            } catch (SQLException exception){
                logger.error(exception.getMessage());
            }
            statement.execute(insertSqlColumn5);
            showUser(connection);
            statement.execute(updateSql);
            ResultSet resultado = statement.executeQuery("SELECT * FROM USUARIO WHERE sobrenome = 'Ferrari'");

            while(resultado.next()){
                logger.debug("O usuario: " + resultado.getString(2) + " foi atualizado!");
            }

            statement.execute(deleteSql);
            logger.info("O usuario com ID=1 foi deletado");
            statement.execute("DELETE FROM USUARIO WHERE EMAIL = 'less@dh.com'");
            logger.info("O usuario Leshly Ontiveros foi deletado");

            showUser(connection);

        } catch (SQLException exception){
            logger.error(exception.getMessage());
        }




    }

    public static void showUser(Connection connection){
        try(Statement statement = connection.createStatement();  ResultSet result = statement.executeQuery(selectSql)){
            while(result.next()){
                logger.info("ID: "+ result.getString(1) +" Nome: " + result.getString(2) + " Sobrenome: "+result.getString(3) + " Idade: " +result.getString(4) + " E-mail: " + result.getString(5));
            }
        } catch (SQLException exception){
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
