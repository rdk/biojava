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
 * Created on 01-21-2010
 */
package org.biojava.nbio.core.sequence.io;

import org.biojava.nbio.core.sequence.io.template.SequenceParserInterface;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Used to parse a stream of a fasta file to get the sequence
 * @author Scooter Willis 
 */
public class FastaSequenceParser implements SequenceParserInterface {

	@Override
	public String getSequence(BufferedReader bufferedReader, int sequenceLength) throws IOException {
		StringBuilder sb;
		if (sequenceLength != -1) {
			sb = new StringBuilder(sequenceLength);
		} else {
			sb = new StringBuilder();
		}
		boolean keepGoing = true;
		while (keepGoing) {
			String line = bufferedReader.readLine();
			if (line == null || line.startsWith(">")) {
				break;
			}
			sb.append(line.trim());
		}
		return sb.toString();
	}
}
