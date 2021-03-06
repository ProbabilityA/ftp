package com.pa.ftpserver.ftp.constant;

/**
 * @author pa
 * @date 2021/6/4 13:50
 */
public enum ResponseMessage {

    OPEN_CHANNEL("150 Opening data channel.\r\n"),

    PORT("200 PORT command successful.\r\n"),
    SERVICE_READY("220 Service ready for new user.\r\n"),
    OKAY("211 Okay.\r\n"),
    VERSION("215 UNIX Type: L8.\r\n"),
    QUIT("221 Goodbye.\r\n"),
    TRANSFER_COMPLETE("226 Transfer complete.\r\n"),
    PASSIVE_MODE("227 Entering Passive Mode (%s,%d,%d)\r\n"),
    LOGIN_PROCEED("230 User logged in, proceed.\r\n"),
    CWD_SUCCESS("250 CWD command successful.\r\n"),
    WORKING_DIRECTORY("257 \"%s\" is current directory.\r\n"),

    NEED_PASSWORD("331 User name okay, need password.\r\n"),
    NEED_ACCOUNT("332 Need account for login.\r\n"),

    CONNECTION_FAILED("425 Can't open data connection.\r\n"),
    NO_SPACE("452 Requested action not taken, Insufficient storage space in system.\r\n"),

    INVALID_PARAM("501 Syntax error in parameters or arguments.\r\n"),
    NOT_SUPPORTED("502 Command not implemented.\r\n"),
    BAD_COMMAND("503 Bad sequence of commands.\r\n"),
    LOGIN_FAILED("530 Login or password incorrect!\r\n"),
    FILE_NOT_FOUND("550 Requested action not taken, file not found.\r\n"),
    DIRECTORY_NOT_FOUND("550 Requested action not taken, directory not found.\r\n"),
    DUPLICATE_FILE_NAME("553 Requested action not taken, File name not allowed.\r\n"),

    TOO_MANY_USERS("10068 Too many users, server is full.\r\n")
    ;

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
