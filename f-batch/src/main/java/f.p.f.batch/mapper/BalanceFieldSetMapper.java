package f.p.f.batch.mapper;

import f.p.f.core.domain.Balance;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BalanceFieldSetMapper implements FieldSetMapper<Balance> {
    @Override
    public Balance mapFieldSet(FieldSet fieldSet) throws BindException {
        return Balance
                .builder()
                .exer(fieldSet.readRawString(0))
                .ident(fieldSet.readRawString(1))
                .nDept(fieldSet.readRawString(2))
                .lBudg(fieldSet.readRawString(3))
                .insee(fieldSet.readRawString(4))
                .siren(fieldSet.readRawString(5))
                .cRegi(fieldSet.readRawString(6))
                .nomen(fieldSet.readRawString(7))
                .cType(fieldSet.readRawString(8))
                .cstyp(fieldSet.readRawString(9))
                .cActi(fieldSet.readRawString(10))
                .finess(fieldSet.readRawString(11))
                .secteur(fieldSet.readRawString(12))
                .cBudg(fieldSet.readRawString(13))
                .codBud1(fieldSet.readRawString(14))
                .compte(fieldSet.readRawString(15))
                .BEDeb(fieldSet.readRawString(16))
                .BECre(fieldSet.readDouble(17))
                .OBNetDeb(fieldSet.readDouble(18))
                .OBNetCre(fieldSet.readDouble(19))
                .ONBDeb(fieldSet.readDouble(20))
                .ONBCre(fieldSet.readDouble(21))
                .OOBDeb(fieldSet.readDouble(22))
                .OOBCre(fieldSet.readDouble(23))
                .sd(fieldSet.readDouble(24))
                .sc(fieldSet.readDouble(25))
                .build();
    }
}
