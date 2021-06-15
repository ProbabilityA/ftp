package com.pa.ftpserver.ftp.constant;

/**
 * @author pa
 * @date 2021/6/4 13:50
 */
public enum ResponseMessage {

    SERVICE_READY("220 Service ready for new user.\r\n"),
    OKAY("211 Okay.\r\n"),
    LOGIN_PROCEED("230 User logged in, proceed.\r\n"),

    NEED_PASSWORD("331 User name okay, need password.\r\n"),
    NEED_ACCOUNT("332 Need account for login.\r\n"),

    INVALID_PARAM("501 Syntax error in parameters or arguments.\r\n"),
    NOT_SUPPORTED("502 Command not implemented.\r\n"),
    BAD_COMMAND("503 Bad sequence of commands.\r\n"),
    LOGIN_FAILED("530 Login or password incorrect!\r\n")
    ;

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    };

    public String getMessage() {
        return message;
    }
}
