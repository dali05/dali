package f.p.f.core.domain;

public enum TypeCollectivite {

    COMMUNE("Commune"),
    SYND("Synd"),
    GFP("GFP"),
    DEP("Dep"),
    REG("Reg");


    /*private String code;*/
    private String libelle;

    TypeCollectivite(String libelle){
        this.libelle = libelle;
    }

}
