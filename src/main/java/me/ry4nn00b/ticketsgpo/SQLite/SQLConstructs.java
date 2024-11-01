package me.ry4nn00b.ticketsgpo.SQLite;

import me.ry4nn00b.ticketsgpo.Main;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLConstructs {

    public static String prefix = Main.prefix;
    private static final Connection con = SQLConnect.con;

    public static void CreateTable() {
        try (PreparedStatement stm = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `empireTicketsGPO`(MemberID VARCHAR(18), gpoTicket TEXT);"
        )) {
            stm.executeUpdate();
            System.out.println(prefix + "A tabela foi carregada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(prefix + "Não foi possível gerar a tabela, verifique o código!");
        }
    }

    // Member Register
    public static boolean hasMemberTable(Member member) {
        String query = "SELECT `MemberID` FROM `empireTicketsGPO` WHERE `MemberID` = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setLong(1, member.getIdLong());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se o membro existe: " + member.getIdLong());
            e.printStackTrace();
            return false;
        }
    }
    public static void addMemberTable(Member member) {
        String query = "INSERT INTO `empireTicketsGPO`(`MemberID`, `gpoTicket`) VALUES (?, 'false')";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setLong(1, member.getIdLong());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar membro: " + member.getIdLong());
            e.printStackTrace();
        }
    }

    //Ticket Getter and Setter
    public static String getTicket(Member member, String ticketType) {
        String query = "SELECT `" + ticketType + "` FROM `empireTicketsGPO` WHERE `MemberID` = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setLong(1, member.getIdLong());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(ticketType);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ticket " + ticketType + " para o membro: " + member.getIdLong());
            e.printStackTrace();
        }
        return "false";
    }
    public static void setTicket(Member member, String ticketType, String ticketID) {
        String query = "UPDATE `empireTicketsGPO` SET `" + ticketType + "` = ? WHERE `MemberID` = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, ticketID);
            statement.setLong(2, member.getIdLong());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar ticket " + ticketType + " para o membro: " + member.getIdLong());
            e.printStackTrace();
        }
    }

}
