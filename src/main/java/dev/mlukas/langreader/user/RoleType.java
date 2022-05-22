package dev.mlukas.langreader.user;

/**
 * Enumeration of types of roles for users in the app. Used for security and authorozation to the app's resources.
 */
enum RoleType {
    /**
     * User is a regular client of the app. Can access all the public or regular user authorized resources.
     */
    USER("ROLE_USER"),
    /**
     * Administrator of the app. Can access admin interface after login.
     */
    ADMIN("ROLE_ADMIN");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    /**
     * Returns the role name used internally by Spring Security, and in the database.
     * @return internal name of the role
     */
    public String getName() {
        return name;
    }
}
