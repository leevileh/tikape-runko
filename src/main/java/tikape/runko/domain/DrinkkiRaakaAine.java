/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author sami
 */
public class DrinkkiRaakaAine {

    private Integer jarjestys;
    private String maara;

    public DrinkkiRaakaAine(Integer jarjestys, String maara) {
        this.jarjestys = jarjestys;
        this.maara = maara;
    }

    public Integer getJarjestys() {
        return this.jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getMaara() {
        return this.maara;
    }
    
    

}
