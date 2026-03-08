package enums;

public enum Program {
    CSE,
    AI,
    SWE;

    public static boolean isValid(String program) {
        for (Program p : Program.values()) {
            if (p.name().equalsIgnoreCase(program)) {
                return true;
            }
        }
        return false;
    }
}
