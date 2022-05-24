package dev.mlukas.langreader.user;

/**
 * Enumeration of types of roles for users in the app. Used for security and authorozation to the app's resources.
 */
enum RoleType {
    /**
     * User is a regular client of the app. Can access all the public or regular user authorized resources.
     */
    ROLE_USER,
    /**
     * Administrator of the app. Can access admin interface after login.
     */
    ROLE_ADMIN
}
