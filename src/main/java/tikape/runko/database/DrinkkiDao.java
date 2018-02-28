/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Drinkki;

public class DrinkkiDao implements Dao<Drinkki, Integer> {

    private Database database;

    public DrinkkiDao(Database database) {
        this.database = database;
    }
    
    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:Drinkit.db");
    }

    @Override
    public Drinkki findOne(Integer key) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String ohje = rs.getString("ohje");

        Drinkki d = new Drinkki(id, nimi, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return d;
    }
    
    public Drinkki haeNimella(String nimi) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String drinkinNimi = rs.getString("nimi");
        String ohje = rs.getString("ohje");

        Drinkki d = new Drinkki(id, drinkinNimi, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return d;
    }

    @Override
    public List<Drinkki> findAll() throws SQLException {

        Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki");

        ResultSet rs = stmt.executeQuery();
        List<Drinkki> drinkit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String ohje = rs.getString("ohje");

            drinkit.add(new Drinkki(id, nimi, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
