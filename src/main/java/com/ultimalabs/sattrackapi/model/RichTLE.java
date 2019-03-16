package com.ultimalabs.sattrackapi.model;

import org.orekit.propagation.analytical.tle.TLE;

import java.util.Objects;

/**
 * TLE with satellite name and string lines validation
 */
public class RichTLE extends TLE {

    /**
     * Satellite name
     */
    private String name;

    public RichTLE(String name, String line1, String line2) {
        super(line1, line2);
        this.name = name;
    }

    /**
     * Returns satellite name
     *
     * @return satellite name
     */
    public String getName() {
        return name;
    }

    /**
     * Check whether string is a valid satellite name
     * <p>
     * The name should be at least three characters long and cannot
     * start with "1" or "2".
     *
     * @param text a string containing satellite name
     * @return true if name is valid
     */
    public static boolean isValidSatelliteName(String text) {
        if (text == null) {
            return false;
        }

        if (text.startsWith("1") || text.startsWith("2")) {
            return false;
        }

        return text.length() >= 3;
    }

    /**
     * Checks whether a string looks like a TLE line
     * <p>
     * The method only checks the line length and first character,
     * which should be either '1' or '2'. Use static method isFormatOK()
     * to check the line content.
     *
     * @param line      the line that's being checked
     * @param whichLine which line - 1 or 2
     * @return true if the line looks like a TLE line
     */
    public static boolean looksLikeTleLine(String line, int whichLine) {
        if (line == null) {
            return false;
        }

        if (line.length() != 69) {
            return false;
        }

        return line.charAt(0) == whichLine + '0';

    }

    /**
     * Check if this TLE equals the provided TLE
     *
     * @param o other TLE
     * @return true if TLEs are equal
     */
    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof RichTLE)) {
            return false;
        }

        final RichTLE tle = (RichTLE) o;

        return (super.equals(o) && tle.getName().equals(this.name));
    }

    /**
     * Get a hashcode for this TLE
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, super.hashCode());
    }

}
