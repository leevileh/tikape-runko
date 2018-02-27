/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.DrinkkiRaakaAine;
import tikape.runko.domain.RaakaAine;

public class DrinkkiRaakaAineDao implements Dao<DrinkkiRaakaAine, Integer> {

    private Database database;

    public DrinkkiRaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public DrinkkiRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");

        DrinkkiRaakaAine d = new DrinkkiRaakaAine(jarjestys, maara);

        rs.close();
        stmt.close();
        connection.close();

        return d;
    }

    @Override
    public List<DrinkkiRaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<DrinkkiRaakaAine> drinkkiRaakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");

            drinkkiRaakaAineet.add(new DrinkkiRaakaAine(jarjestys, maara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkkiRaakaAineet;
    }

    public List<String> drinkinRaakaAineet(String n) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT RaakaAine.nimi, DrinkkiRaakaAine.maara "
                + "FROM DrinkkiRaakaAine, RaakaAine, Drinkki "
                + "WHERE DrinkkiRaakaAine.drinkki_id = Drinkki.id "
                + "AND DrinkkiRaakaAine.raakaAine_id = RaakaAine.id "
                + "AND Drinkki.nimi = ?");
        stmt.setString(1, n);

        ResultSet rs = stmt.executeQuery();
        List<String> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            String nimi = rs.getString("nimi");
            String maara = rs.getString("maara");

            raakaAineet.add(nimi + ", " + maara);
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

}
