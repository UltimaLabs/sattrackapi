package com.ultimalabs.sattrackapi.model;

import org.orekit.propagation.analytical.tle.TLE;

import java.util.Objects;

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
