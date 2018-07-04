package f.p.f.batch.exception;

import lombok.Data;

@Data
public class BatchException extends Exception {

    public BatchException(String var1){
        super(var1);
    }

}
