package me.ry4nn00b.ticketsgpo.SQLite;

import me.ry4nn00b.ticketsgpo.Main;
import org.sqlite.jdbc4.JDBC4DatabaseMetaData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLConnect {

    public static String prefix = Main.prefix;
    public static Connection con;
    private static final Logger logger = Logger.getLogger(JDBC4DatabaseMetaData.class.getName());

    public static Connection openConnection() {
        String url = "jdbc:sqlite:EmpireTicketsGPO.db";
        try {
            con = DriverManager.getConnection(url);
            SQLConstructs.CreateTable();
            logger.info(prefix + "Conexão estabelecida com sucesso!");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, prefix + "Não foi possível estabelecer uma conexão!", e);
        }
        return con;
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                logger.info(prefix + "Conexão fechada com sucesso.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, prefix + "Erro ao fechar a conexão!", e);
            }
        }
    }

}
