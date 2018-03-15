package shared;

import java.io.Serializable;

public enum MessageType implements Serializable {
    INFORMATION(0),
    WARNING(1),
    ERROR(2),;

    private int code;
    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
