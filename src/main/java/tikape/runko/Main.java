package tikape.runko;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.DrinkkiDao;
import tikape.runko.database.DrinkkiRaakaAineDao;
import tikape.runko.domain.Drinkki;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:Drinkit.db");
//        database.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(database);
        DrinkkiRaakaAineDao draDao = new DrinkkiRaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/drinkit", (req, res) -> {
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
            Connection conn = database.getConnection();
            PreparedStatement stmnt = conn.prepareStatement("INSERT INTO Drinkki (nimi, ohje) VALUES (?, ?)");
            stmnt.setString(1, req.queryParams("nimi"));
            stmnt.setString(2, req.queryParams("ohje"));
            stmnt.executeUpdate();
            conn.close();
            res.redirect("/drinkit");
            return "";
        });
    }
}
