package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.DrinkkiDao;
import tikape.runko.database.DrinkkiRaakaAineDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Drinkki;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static String drinkkiNimi = "";
    public static Map<String, String> raakaAineValimuisti = new HashMap<>();
    
    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:Drinkit.db");
    }

    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        

        Database database = new Database("jdbc:sqlite:Drinkit.db");
//        database.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(database);
        DrinkkiRaakaAineDao draDao = new DrinkkiRaakaAineDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/drinkit", (req, res) -> {
            raakaAineValimuisti.clear();
            drinkkiNimi = "";
            HashMap map = new HashMap<>();
            map.put("drinkit", drinkkiDao.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Drinkki d = drinkkiDao.findOne(Integer.parseInt(req.params("id")));
            map.put("drinkki", d);
            map.put("raakaAineet", draDao.drinkinRaakaAineet(d.getNimi()));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

        post("/drinkit", (req, res) -> {
            drinkkiNimi = req.queryParams("nimi");

            if (!drinkkiNimi.isEmpty()) {
                res.redirect("/raakaAineet");
                return "";
            } else {
                res.redirect("/drinkit");
                return "";
            }

        });

        get("/raakaAineet", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Drinkki> drinkit = drinkkiDao.findAll();
            map.put("viimeinen", drinkkiNimi);
            raakaAineValimuisti.entrySet().stream().forEach(r -> {
                map.put(r.getKey(), r.getValue());
            });

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());

        post("/raakaAineet", (req, res) -> {
            if (!req.queryParams("nimi").isEmpty() && !req.queryParams("määrä").isEmpty()) {
                raakaAineValimuisti.put(req.queryParams("nimi"), req.queryParams("määrä"));
                res.redirect("/raakaAineet");
                return "";
            } else {
                res.redirect("/raakaAineet");
                return "";
            }

        });

        get("/luoValmistusOhje", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Drinkki> drinkit = drinkkiDao.findAll();
            map.put("viimeinen", drinkkiNimi);

            map.put("luoValmistusOhje", draDao.drinkinRaakaAineet(drinkkiNimi));

            return new ModelAndView(map, "luoValmistusOhje");
        }, new ThymeleafTemplateEngine());

        post("/luoValmistusOhje", (req, res) -> {
            Connection conn = getConnection();

            String drinkkiOhje = req.queryParams("ohje");
            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO Drinkki (nimi, ohje) VALUES (?,?)");
            stmnt.setString(1, drinkkiNimi);
            stmnt.setString(2, drinkkiOhje);
            stmnt.executeUpdate();

            stmnt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            List<String> nimet = new ArrayList<>();
            raakaAineValimuisti.entrySet().stream().forEach(r -> {
                nimet.add(r.getKey());
            });
            for (int i = 0; i < nimet.size(); i++) {
                stmnt.setString(1, nimet.get(i));
                stmnt.executeUpdate();
            }

            stmnt = conn.prepareStatement("INSERT INTO DrinkkiRaakaAine (drinkki_id, raakaAine_id, maara) VALUES (?, ?, ?)");
            for (int i = 0; i < raakaAineValimuisti.size(); i++) {
                stmnt.setInt(1, drinkkiDao.haeNimella(drinkkiNimi).getId());
                RaakaAine ra = raakaAineDao.haeNimella(nimet.get(i));
                stmnt.setInt(2, ra.getId());
                stmnt.setString(3, raakaAineValimuisti.get(ra.getNimi()));
                stmnt.executeUpdate();
            }

            res.redirect("/drinkit");
            return "";
        });
    }
}
