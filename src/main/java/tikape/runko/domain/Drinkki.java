package tikape.runko.domain;

public class Drinkki {

    private Integer id;
    private String nimi;
    private String ohje;

    public Drinkki(Integer id, String nimi, String ohje) {
        this.id = id;
        this.nimi = nimi;
        this.ohje = ohje;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    public String getOhje() {
        return this.ohje;
    }
    

}
