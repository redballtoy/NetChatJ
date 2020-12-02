package autoidentification;

public  enum Prefics {
    AUTH_CMD_PREFIX("/auth"),
    AUTHOK_CMD_PREFIX("/authok"),
    AUTHERR_CMD_PREFIX("/autherr"),
    PRIVATE_MESSAGE_PREFIX("/write"),
    END_CMD("/end"),
    CLIENT_MSG_PREFIX("/cl_msg"),
    SERVER_MSG_PREFIX("/srv_msg"),
    STRING_SPLIT_PREFIX ("\\s+"); //символ пробела в неограниченном количестве



    private String code;

    Prefics(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }


}
