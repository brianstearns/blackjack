package util;
public enum Color {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"), 
    BLUE("\u001B[34m"), 
    YELLOW("\u001B[33m"), 
    CYAN("\u001B[36m"),
    MAGENTA("\u001B[35m"), 
    LIGHT_GREY("\u001B[90m"),
    RESET("\u001B[0m");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
