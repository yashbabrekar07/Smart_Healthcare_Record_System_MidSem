package hms.model;

// Interface for live search matching
public interface Searchable {
    boolean matches(String key);
}
