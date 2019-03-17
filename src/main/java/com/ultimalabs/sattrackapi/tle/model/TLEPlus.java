package com.ultimalabs.sattrackapi.tle.model;

import org.orekit.errors.OrekitException;
import org.orekit.errors.OrekitMessages;
import org.orekit.propagation.analytical.tle.TLE;

import java.util.Objects;

/**
 * Extended TLE
 * <p>
 * Added satellite name, TLE lines validation, international
 * designator and raw TLE data getters.
 */
public class TLEPlus extends TLE {

    /**
     * Satellite name
     */
    private String name;

    public TLEPlus(String title, String line1, String line2) {
        super(line1, line2);

        if (title == null) {
            this.name = "";
        } else {
            this.name = title.trim();
        }
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
    public static boolean isValidSatelliteTitle(String text) {

        if (text == null) {
            return false;
        }

        if (looksLikeTleLine(text, 1) || looksLikeTleLine(text, 2)) {
            return false;
        }

        return text.length() >= 3;
    }

    /**
     * Returns international designator
     * <p>
     * Please ot that the launch piece is not padded with
     * trailing whitespace characters.
     *
     * @return international designator
     */
    public String getInternationalDesignator() {

        return ourPadding("launchYear", this.getLaunchYear() % 100, '0', 2, true) +
                ourPadding("launchNumber", this.getLaunchNumber(), '0', 3, true) +
                this.getLaunchPiece();
    }

    public String getTle() {
        if (!getName().equals("")) {
            return ourPadding("name", getName(), ' ', 24, false) +
                    System.lineSeparator() + getLine1() + System.lineSeparator() + getLine2();
        }

        return getLine1() + System.lineSeparator() + getLine2();

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
     * Add padding characters before an integer
     * <p>
     * Copied from upstream TLE.java because their is private.
     *
     * @param name           parameter name
     * @param k              integer to pad
     * @param c              padding character
     * @param size           desired size
     * @param rightJustified if true, the resulting string is
     *                       right justified (i.e. space are added to the left)
     * @return padded string
     */
    private String ourPadding(final String name, final int k, final char c,
                              final int size, final boolean rightJustified) {
        return ourPadding(name, Integer.toString(k), c, size, rightJustified);
    }

    /**
     * Add padding characters to a string
     * <p>
     * Copied from upstream TLE.java because their is private.
     *
     * @param name           parameter name
     * @param string         string to pad
     * @param c              padding character
     * @param size           desired size
     * @param rightJustified if true, the resulting string is
     *                       right justified (i.e. space are added to the left)
     * @return padded string
     */
    private String ourPadding(final String name, final String string, final char c,
                              final int size, final boolean rightJustified) {

        if (string.length() > size) {
            throw new OrekitException(OrekitMessages.TLE_INVALID_PARAMETER,
                    "", name, string);
        }

        final StringBuilder padding = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            padding.append(c);
        }

        if (rightJustified) {
            final String concatenated = padding + string;
            final int l = concatenated.length();
            return concatenated.substring(l - size, l);
        }

        return (string + padding).substring(0, size);
    }


    /**
     * Check if this TLE equals the provided TLE
     *
     * @param o other TLE
     * @return true if TLEs are equal
     */
    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof TLEPlus)) {
            return false;
        }

        return (super.equals(o) && this.name.equals(((TLEPlus) o).getName()));

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
