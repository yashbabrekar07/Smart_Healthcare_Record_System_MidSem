package hms.util;

// Input validation utility class throwing IllegalArgumentException
public class Validator {

    public static void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }

    public static void validateAge(int age) throws IllegalArgumentException {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0 and 120.");
        }
    }

    public static void validatePhone(String phone) throws IllegalArgumentException {
        if (phone == null || phone.trim().length() != 10) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Phone number must contain only numeric digits.");
            }
        }
    }

    public static void validateDate(String date) throws IllegalArgumentException {
        if (date == null || date.length() != 10 || date.charAt(4) != '-' || date.charAt(7) != '-') {
            throw new IllegalArgumentException("Date must follow format YYYY-MM-DD.");
        }
        for (int i = 0; i < 10; i++) {
            if (i == 4 || i == 7) continue;
            char c = date.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Date must contain valid digits (YYYY-MM-DD).");
            }
        }
    }

    public static void validateBloodGroup(String bg) throws IllegalArgumentException {
        if (bg == null || bg.trim().length() == 0) {
            throw new IllegalArgumentException("Blood group cannot be empty.");
        }
    }
}
