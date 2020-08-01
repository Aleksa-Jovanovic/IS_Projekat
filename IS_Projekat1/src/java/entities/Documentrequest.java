/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aca19
 */
@Entity
@Table(name = "documentrequest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentrequest.findAll", query = "SELECT d FROM Documentrequest d"),
    @NamedQuery(name = "Documentrequest.findByIDdocumentrequest", query = "SELECT d FROM Documentrequest d WHERE d.iDdocumentrequest = :iDdocumentrequest"),
    @NamedQuery(name = "Documentrequest.findByJmbg", query = "SELECT d FROM Documentrequest d WHERE d.jmbg = :jmbg"),
    @NamedQuery(name = "Documentrequest.findByIme", query = "SELECT d FROM Documentrequest d WHERE d.ime = :ime"),
    @NamedQuery(name = "Documentrequest.findByPrezime", query = "SELECT d FROM Documentrequest d WHERE d.prezime = :prezime"),
    @NamedQuery(name = "Documentrequest.findByImemajke", query = "SELECT d FROM Documentrequest d WHERE d.imemajke = :imemajke"),
    @NamedQuery(name = "Documentrequest.findByPrezimemajke", query = "SELECT d FROM Documentrequest d WHERE d.prezimemajke = :prezimemajke"),
    @NamedQuery(name = "Documentrequest.findByImeoca", query = "SELECT d FROM Documentrequest d WHERE d.imeoca = :imeoca"),
    @NamedQuery(name = "Documentrequest.findByPrezimeoca", query = "SELECT d FROM Documentrequest d WHERE d.prezimeoca = :prezimeoca"),
    @NamedQuery(name = "Documentrequest.findByPol", query = "SELECT d FROM Documentrequest d WHERE d.pol = :pol"),
    @NamedQuery(name = "Documentrequest.findByDatumrodjenja", query = "SELECT d FROM Documentrequest d WHERE d.datumrodjenja = :datumrodjenja"),
    @NamedQuery(name = "Documentrequest.findByNacionalnost", query = "SELECT d FROM Documentrequest d WHERE d.nacionalnost = :nacionalnost"),
    @NamedQuery(name = "Documentrequest.findByProfesija", query = "SELECT d FROM Documentrequest d WHERE d.profesija = :profesija"),
    @NamedQuery(name = "Documentrequest.findByBracnostanje", query = "SELECT d FROM Documentrequest d WHERE d.bracnostanje = :bracnostanje"),
    @NamedQuery(name = "Documentrequest.findByOpstinaprebivalista", query = "SELECT d FROM Documentrequest d WHERE d.opstinaprebivalista = :opstinaprebivalista"),
    @NamedQuery(name = "Documentrequest.findByUlicaprebivalista", query = "SELECT d FROM Documentrequest d WHERE d.ulicaprebivalista = :ulicaprebivalista"),
    @NamedQuery(name = "Documentrequest.findByBrojprebivalista", query = "SELECT d FROM Documentrequest d WHERE d.brojprebivalista = :brojprebivalista"),
    @NamedQuery(name = "Documentrequest.findByStatus", query = "SELECT d FROM Documentrequest d WHERE d.status = :status")})
public class Documentrequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_documentrequest")
    private Integer iDdocumentrequest;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "JMBG")
    private String jmbg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Ime")
    private String ime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Prezime")
    private String prezime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Ime_majke")
    private String imemajke;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Prezime_majke")
    private String prezimemajke;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Ime_oca")
    private String imeoca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Prezime_oca")
    private String prezimeoca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Pol")
    private String pol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "Datum_rodjenja")
    private String datumrodjenja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Nacionalnost")
    private String nacionalnost;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Profesija")
    private String profesija;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Bracno_stanje")
    private String bracnostanje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Opstina_prebivalista")
    private String opstinaprebivalista;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Ulica_prebivalista")
    private String ulicaprebivalista;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Broj_prebivalista")
    private String brojprebivalista;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Status")
    private String status;

    public Documentrequest() {
    }

    public Documentrequest(Integer iDdocumentrequest) {
        this.iDdocumentrequest = iDdocumentrequest;
    }

    public Documentrequest(Integer iDdocumentrequest, String jmbg, String ime, String prezime, String imemajke, String prezimemajke, String imeoca, String prezimeoca, String pol, String datumrodjenja, String nacionalnost, String profesija, String bracnostanje, String opstinaprebivalista, String ulicaprebivalista, String brojprebivalista, String status) {
        this.iDdocumentrequest = iDdocumentrequest;
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.imemajke = imemajke;
        this.prezimemajke = prezimemajke;
        this.imeoca = imeoca;
        this.prezimeoca = prezimeoca;
        this.pol = pol;
        this.datumrodjenja = datumrodjenja;
        this.nacionalnost = nacionalnost;
        this.profesija = profesija;
        this.bracnostanje = bracnostanje;
        this.opstinaprebivalista = opstinaprebivalista;
        this.ulicaprebivalista = ulicaprebivalista;
        this.brojprebivalista = brojprebivalista;
        this.status = status;
    }

    public Documentrequest(int newRequestID, ArrayList<String> textFieldData, ArrayList<String> radioButtonData) {
        this.iDdocumentrequest = newRequestID;
        
        this.pol = radioButtonData.get(0);
        this.bracnostanje = radioButtonData.get(1);
        
        this.jmbg = textFieldData.get(0);
        this.ime = textFieldData.get(1);
        this.prezime = textFieldData.get(2);
        this.datumrodjenja = textFieldData.get(3);
        this.nacionalnost = textFieldData.get(4);
        this.profesija = textFieldData.get(5);
        this.opstinaprebivalista = textFieldData.get(6);
        this.ulicaprebivalista = textFieldData.get(7);
        this.brojprebivalista = textFieldData.get(8);
        this.imeoca = textFieldData.get(9);
        this.prezimeoca = textFieldData.get(10);
        this.imemajke = textFieldData.get(11);
        this.prezimemajke = textFieldData.get(12);
        
        this.status = "Kreiran";
    }

    public Integer getIDdocumentrequest() {
        return iDdocumentrequest;
    }

    public void setIDdocumentrequest(Integer iDdocumentrequest) {
        this.iDdocumentrequest = iDdocumentrequest;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getImemajke() {
        return imemajke;
    }

    public void setImemajke(String imemajke) {
        this.imemajke = imemajke;
    }

    public String getPrezimemajke() {
        return prezimemajke;
    }

    public void setPrezimemajke(String prezimemajke) {
        this.prezimemajke = prezimemajke;
    }

    public String getImeoca() {
        return imeoca;
    }

    public void setImeoca(String imeoca) {
        this.imeoca = imeoca;
    }

    public String getPrezimeoca() {
        return prezimeoca;
    }

    public void setPrezimeoca(String prezimeoca) {
        this.prezimeoca = prezimeoca;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getDatumrodjenja() {
        return datumrodjenja;
    }

    public void setDatumrodjenja(String datumrodjenja) {
        this.datumrodjenja = datumrodjenja;
    }

    public String getNacionalnost() {
        return nacionalnost;
    }

    public void setNacionalnost(String nacionalnost) {
        this.nacionalnost = nacionalnost;
    }

    public String getProfesija() {
        return profesija;
    }

    public void setProfesija(String profesija) {
        this.profesija = profesija;
    }

    public String getBracnostanje() {
        return bracnostanje;
    }

    public void setBracnostanje(String bracnostanje) {
        this.bracnostanje = bracnostanje;
    }

    public String getOpstinaprebivalista() {
        return opstinaprebivalista;
    }

    public void setOpstinaprebivalista(String opstinaprebivalista) {
        this.opstinaprebivalista = opstinaprebivalista;
    }

    public String getUlicaprebivalista() {
        return ulicaprebivalista;
    }

    public void setUlicaprebivalista(String ulicaprebivalista) {
        this.ulicaprebivalista = ulicaprebivalista;
    }

    public String getBrojprebivalista() {
        return brojprebivalista;
    }

    public void setBrojprebivalista(String brojprebivalista) {
        this.brojprebivalista = brojprebivalista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDdocumentrequest != null ? iDdocumentrequest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documentrequest)) {
            return false;
        }
        Documentrequest other = (Documentrequest) object;
        if ((this.iDdocumentrequest == null && other.iDdocumentrequest != null) || (this.iDdocumentrequest != null && !this.iDdocumentrequest.equals(other.iDdocumentrequest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Documentrequest[ iDdocumentrequest=" + iDdocumentrequest + " ]";
    }
    
}
