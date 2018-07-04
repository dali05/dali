package f.p.f.core.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Balance implements Serializable {
    private int index;
    private String exer;
    private String ident;
    private String nDept;
    private String lBudg;
    private String insee;
    private String siren;
    private String cRegi;
    private String nomen;
    private String cType;
    private String cstyp;
    private String cActi;
    private String finess;
    private String secteur;
    private String cBudg;
    private String codBud1;
    private String compte;
    private String BEDeb;
    private Double BECre;
    private Double OBNetDeb;
    private Double OBNetCre;
    private Double ONBDeb;
    private Double ONBCre;
    private Double OOBDeb;
    private Double OOBCre;
    private Double sd;
    private Double sc;
}
