package clase;

import java.util.HashMap;
import java.util.Map;

public enum TipContBancar {
    CURENT,
    ECONOMII,
    DEPOZIT,
    CREDIT;

    public static final Map<String,TipContBancar> tipContBancarMap = new HashMap<>();
    static {
        for (TipContBancar contBancar : TipContBancar.values())
            tipContBancarMap.put(contBancar.toString(), contBancar);
    }
}


