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
package org.biojava.nbio.structure.io;

import static org.junit.Assert.*;

//import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.biojava.nbio.structure.AminoAcidImpl;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.ResidueNumber;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.cif.CifStructureConverter;
import org.junit.Test;

public class TestMmCIFSpecialCases {

	/**
	 * This tests for cases where dots appear in integer fields.
	 * Unusual but it happens in some PDB entries like 1s32
	 * See issue https://github.com/biojava/biojava/issues/368
	 * See also http://mmcif.wwpdb.org/docs/tutorials/mechanics/pdbx-mmcif-syntax.html :
	 * "A period (.) may be used to identify that there is no appropriate value for the item or that a value has been intentionally omitted."
	 * @throws IOException
	 */
	@Test
	public void testDotsInIntFields() throws IOException {

		// taken from 1s32
		String mmcifStr =
				"data_\n" +
				"loop_\n" +
				"_struct_ref_seq_dif.align_id\n" +
				"_struct_ref_seq_dif.pdbx_pdb_id_code\n"+
				"_struct_ref_seq_dif.mon_id\n"+
				"_struct_ref_seq_dif.pdbx_pdb_strand_id\n"+
				"_struct_ref_seq_dif.seq_num\n"+ // integer field that contains '.'
				"_struct_ref_seq_dif.pdbx_seq_db_name\n"+
				"_struct_ref_seq_dif.pdbx_seq_db_accession_code\n"+
				"_struct_ref_seq_dif.db_mon_id\n"+
				"_struct_ref_seq_dif.pdbx_seq_db_seq_num\n"+
				"_struct_ref_seq_dif.details\n"+
				"_struct_ref_seq_dif.pdbx_auth_seq_num\n"+
				"_struct_ref_seq_dif.pdbx_pdb_ins_code\n"+
				"_struct_ref_seq_dif.pdbx_ordinal\n"+
				"1 1S32 . A . GB  30268544 MET 1 'INTIATING METHIONINE' ? ? 1\n"+
				"2 1S32 . E . GB  30268544 MET 1 'INTIATING METHIONINE' ? ? 2\n"+
				"3 1S32 . B . UNP P02304   MET 0 'INTIATING METHIONINE' ? ? 3\n"+
				"4 1S32 . F . UNP P02304   MET 0 'INTIATING METHIONINE' ? ? 4\n"+
				"5 1S32 . C . GB  30268540 MET 1 'INTIATING METHIONINE' ? ? 5\n"+
				"6 1S32 . G . GB  30268540 MET 1 'INTIATING METHIONINE' ? ? 6\n"+
				"7 1S32 . D . GB  30268542 MET 1 'INTIATING METHIONINE' ? ? 7\n"+
				"8 1S32 . H . GB  30268542 MET 1 'INTIATING METHIONINE' ? ? 8" ;

		InputStream stream = new ByteArrayInputStream(mmcifStr.getBytes(StandardCharsets.UTF_8));
		Structure s = CifStructureConverter.fromInputStream(stream);

		// nothing to assert, the test just makes sure it doesn't throw an exception
	}

	/**
	 * If auth_seq_id is uniformly missing, it should get replaced with label_seq_id
	 * @throws IOException
	 */
	@Test
	public void testMissingAuthRes() throws IOException {
		// taken from 1s32
		String mmcifStr =
				"data_\n" +
				"loop_\n" + 
				"_atom_site.group_PDB \n" + 
				"_atom_site.id \n" + 
				"_atom_site.type_symbol \n" + 
				"_atom_site.label_atom_id \n" + 
				"_atom_site.label_alt_id \n" + 
				"_atom_site.label_comp_id \n" + 
				"_atom_site.label_asym_id \n" + 
				"_atom_site.label_entity_id \n" + 
				"_atom_site.label_seq_id \n" + // 9
				"_atom_site.pdbx_PDB_ins_code \n" + 
				"_atom_site.Cartn_x \n" + 
				"_atom_site.Cartn_y \n" + 
				"_atom_site.Cartn_z \n" + 
				"_atom_site.occupancy \n" + 
				"_atom_site.B_iso_or_equiv \n" + 
				"_atom_site.pdbx_formal_charge \n" + 
//				"_atom_site.auth_seq_id \n" + // 17
				"_atom_site.auth_comp_id \n" + 
				"_atom_site.auth_asym_id \n" + 
				"_atom_site.auth_atom_id \n" + 
				"_atom_site.pdbx_PDB_model_num \n" + 
				// 1    2    3 4   5 6   7 8 9   10 11    12      13      14   15     16  18
				"ATOM   1    N N   . SER A 1 1   ? 36.651 10.046  12.372  1.00 59.41  ?  SER A N   1 \n" + 
				"ATOM   2    C CA  . SER A 1 1   ? 37.678 9.064   12.762  1.00 38.34  ?  SER A CA  1 \n" + 
				"ATOM   3    C C   . SER A 1 1   ? 38.289 8.315   11.570  1.00 35.51  ?  SER A C   1 \n" + 
				"ATOM   4    O O   . SER A 1 1   ? 38.067 7.099   11.349  1.00 28.94  ?  SER A O   1 \n" + 
				"ATOM   5    C CB  . SER A 1 1   ? 37.086 8.105   13.777  1.00 37.47  ?  SER A CB  1 \n" + 
				"ATOM   6    O OG  . SER A 1 1   ? 37.600 8.409   15.070  1.00 56.00  ?  SER A OG  1 \n" + 
				"ATOM   7    N N   . MET A 1 2   ? 39.103 9.053   10.823  1.00 28.40  ?  MET A N   1 \n" + 
				"ATOM   8    C CA  . MET A 1 2   ? 39.864 8.468   9.750   1.00 26.64  ?  MET A CA  1 \n" + 
				"ATOM   9    C C   . MET A 1 2   ? 41.336 8.683   10.025  1.00 20.56  ?  MET A C   1 \n" + 
				"ATOM   10   O O   . MET A 1 2   ? 41.754 9.676   10.626  1.00 17.95  ?  MET A O   1 \n" + 
				"ATOM   11   C CB  . MET A 1 2   ? 39.487 9.121   8.424   1.00 26.07  ?  MET A CB  1 \n" + 
				"ATOM   12   C CG  . MET A 1 2   ? 37.997 9.097   8.126   1.00 34.89  ?  MET A CG  1 \n" + 
				"ATOM   13   S SD  . MET A 1 2   ? 37.509 10.484  7.069   1.00 40.57  ?  MET A SD  1 \n" + 
				"ATOM   14   C CE  . MET A 1 2   ? 38.604 10.218  5.678   1.00 41.60  ?  MET A CE  1 \n" + 
				"ATOM   15   N N   . ASN A 1 3   ? 42.136 7.731   9.595   1.00 16.88  ?  ASN A N   1 \n" + 
				"ATOM   16   C CA  . ASN A 1 3   ? 43.573 7.996   9.483   1.00 16.32  ?  ASN A CA  1 \n" + 
				"ATOM   17   C C   . ASN A 1 3   ? 43.851 8.741   8.194   1.00 13.33  ?  ASN A C   1 \n" + 
				"ATOM   18   O O   . ASN A 1 3   ? 43.138 8.574   7.194   1.00 15.89  ?  ASN A O   1 \n" + 
				"ATOM   19   C CB  . ASN A 1 3   ? 44.295 6.652   9.469   1.00 19.42  ?  ASN A CB  1 \n" + 
				"ATOM   20   C CG  . ASN A 1 3   ? 44.172 5.932   10.790  1.00 18.34  ?  ASN A CG  1 \n" + 
				"ATOM   21   O OD1 . ASN A 1 3   ? 44.475 6.496   11.813  1.00 15.88  ?  ASN A OD1 1 \n" + 
				"ATOM   22   N ND2 . ASN A 1 3   ? 43.685 4.690   10.761  1.00 19.73  ?  ASN A ND2 1 \n";

		InputStream stream = new ByteArrayInputStream(mmcifStr.getBytes(StandardCharsets.UTF_8));
		Structure s = CifStructureConverter.fromInputStream(stream);

		assertNotNull(s);
		
		assertEquals(1, s.size());
		for(Chain chain : s.getChains()) {
			assertEquals(1, s.size());
			int pos = 0;
			for( Group g : chain.getAtomGroups()) {
				if (g instanceof AminoAcidImpl) {
					// label_seq_id is stored in the id (biojava 4)
					assertEquals(pos + 1, ((AminoAcidImpl) g).getId());
					assertEquals(new ResidueNumber("A", pos + 1, null), g.getResidueNumber());
				}
				
				pos++;
			}
		}
	}

	@Test
	public void testSomeMissingAuthRes() throws IOException {
		// taken from 1s32
		String mmcifStr =
				"data_\n" +
				"loop_\n" +
				"_atom_site.group_PDB \n" +
				"_atom_site.id \n" +
				"_atom_site.type_symbol \n" +
				"_atom_site.label_atom_id \n" +
				"_atom_site.label_alt_id \n" +
				"_atom_site.label_comp_id \n" +
				"_atom_site.label_asym_id \n" +
				"_atom_site.label_entity_id \n" +
				"_atom_site.label_seq_id \n" + // 9
				"_atom_site.pdbx_PDB_ins_code \n" +
				"_atom_site.Cartn_x \n" +
				"_atom_site.Cartn_y \n" +
				"_atom_site.Cartn_z \n" +
				"_atom_site.occupancy \n" +
				"_atom_site.B_iso_or_equiv \n" +
				"_atom_site.pdbx_formal_charge \n" +
				"_atom_site.auth_seq_id \n" + // 17
				"_atom_site.auth_comp_id \n" +
				"_atom_site.auth_asym_id \n" +
				"_atom_site.auth_atom_id \n" +
				"_atom_site.pdbx_PDB_model_num \n" +
				// 1    2   3 4   5 6   7  8  9   10 11     12      13      14     15   16  18
				"ATOM   1    N N   . SER A 1 1   ? 36.651 10.046  12.372  1.00 59.41  ? 42  SER A N   1 \n" +
				"ATOM   2    C CA  . SER A 1 1   ? 37.678 9.064   12.762  1.00 38.34  ? 42  SER A CA  1 \n" + 
				"ATOM   3    C C   . SER A 1 1   ? 38.289 8.315   11.570  1.00 35.51  ? 42  SER A C   1 \n" + 
				"ATOM   4    O O   . SER A 1 1   ? 38.067 7.099   11.349  1.00 28.94  ? 42  SER A O   1 \n" + 
				"ATOM   5    C CB  . SER A 1 1   ? 37.086 8.105   13.777  1.00 37.47  ? 42  SER A CB  1 \n" + 
				"ATOM   6    O OG  . SER A 1 1   ? 37.600 8.409   15.070  1.00 56.00  ? ?   SER A OG  1 \n" + 
				"ATOM   7    N N   . MET A 1 2   ? 39.103 9.053   10.823  1.00 28.40  ? ?   MET A N   1 \n" + 
				"ATOM   8    C CA  . MET A 1 2   ? 39.864 8.468   9.750   1.00 26.64  ? ?   MET A CA  1 \n" + 
				"ATOM   9    C C   . MET A 1 2   ? 41.336 8.683   10.025  1.00 20.56  ? ?   MET A C   1 \n" + 
				"ATOM   10   O O   . MET A 1 2   ? 41.754 9.676   10.626  1.00 17.95  ? ?   MET A O   1 \n" + 
				"ATOM   11   C CB  . MET A 1 2   ? 39.487 9.121   8.424   1.00 26.07  ? ?   MET A CB  1 \n" + 
				"ATOM   12   C CG  . MET A 1 2   ? 37.997 9.097   8.126   1.00 34.89  ? ?   MET A CG  1 \n" + 
				"ATOM   13   S SD  . MET A 1 2   ? 37.509 10.484  7.069   1.00 40.57  ? ?   MET A SD  1 \n" + 
				"ATOM   14   C CE  . MET A 1 2   ? 38.604 10.218  5.678   1.00 41.60  ? ?   MET A CE  1 \n" + 
				"ATOM   15   N N   . ASN A 1 3   ? 42.136 7.731   9.595   1.00 16.88  ? 44  ASN A N   1 \n" + 
				"ATOM   16   C CA  . ASN A 1 3   ? 43.573 7.996   9.483   1.00 16.32  ? 44  ASN A CA  1 \n" + 
				"ATOM   17   C C   . ASN A 1 3   ? 43.851 8.741   8.194   1.00 13.33  ? 44  ASN A C   1 \n" + 
				"ATOM   18   O O   . ASN A 1 3   ? 43.138 8.574   7.194   1.00 15.89  ? 44  ASN A O   1 \n" + 
				"ATOM   19   C CB  . ASN A 1 3   ? 44.295 6.652   9.469   1.00 19.42  ? 44  ASN A CB  1 \n" + 
				"ATOM   20   C CG  . ASN A 1 3   ? 44.172 5.932   10.790  1.00 18.34  ? 44  ASN A CG  1 \n" + 
				"ATOM   21   O OD1 . ASN A 1 3   ? 44.475 6.496   11.813  1.00 15.88  ? 44  ASN A OD1 1 \n" + 
				"ATOM   22   N ND2 . ASN A 1 3   ? 43.685 4.690   10.761  1.00 19.73  ? 44  ASN A ND2 1 \n";

		InputStream stream = new ByteArrayInputStream(mmcifStr.getBytes(StandardCharsets.UTF_8));



		try {
			// Current behaviour is to disallow ? in auth_seq_id
			Structure s = CifStructureConverter.fromInputStream(stream);
			fail("? in auth_seq_id not permitted");
		} catch(NumberFormatException e) {
			// expected
		} 
	}

}
