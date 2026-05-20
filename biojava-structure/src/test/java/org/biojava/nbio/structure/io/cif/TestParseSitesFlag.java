/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.nbio.structure.io.cif;

import org.biojava.nbio.structure.Site;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.CifFileReader;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link FileParsingParameters#setParseSites(boolean)} flag
 * in the CIF parser.
 */
public class TestParseSitesFlag {

    private static final String RESOURCE = "/org/biojava/nbio/structure/io/mmcif/1stp_v5.cif";

    @Test
    public void parseSitesDefaultTrueProducesSites() throws IOException {
        Structure s = readStructure(new FileParsingParameters());

        List<Site> sites = s.getSites();
        assertNotNull("sites list should not be null when parseSites=true", sites);
        assertTrue("expected at least one site to be parsed from 1stp", !sites.isEmpty());
        // 1stp has a single _struct_site (AC1) with several _struct_site_gen rows
        assertTrue("expected at least one site residue", sites.get(0).getGroups() != null
                && !sites.get(0).getGroups().isEmpty());
    }

    @Test
    public void parseSitesFalseSkipsAllSiteData() throws IOException {
        FileParsingParameters params = new FileParsingParameters();
        params.setParseSites(false);

        Structure s = readStructure(params);

        List<Site> sites = s.getSites();
        // Either the list is null/empty: no sites at all (neither struct_site nor struct_site_gen)
        assertTrue("expected no sites when parseSites=false but got " + (sites == null ? "null" : sites.size()),
                sites == null || sites.isEmpty());
    }

    @Test
    public void parseSitesGetterReflectsSetter() {
        FileParsingParameters params = new FileParsingParameters();
        assertEquals(true, params.isParseSites());
        params.setParseSites(false);
        assertEquals(false, params.isParseSites());
        params.setParseSites(true);
        assertEquals(true, params.isParseSites());
    }

    private Structure readStructure(FileParsingParameters params) throws IOException {
        InputStream in = getClass().getResourceAsStream(RESOURCE);
        Objects.requireNonNull(in, "could not acquire test resource " + RESOURCE);
        CifFileReader reader = new CifFileReader();
        reader.setFileParsingParameters(params);
        return reader.getStructure(in);
    }
}
