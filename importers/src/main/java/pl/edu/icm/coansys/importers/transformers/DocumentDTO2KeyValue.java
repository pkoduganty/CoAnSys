/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentDTO;

/**
 * @author pdendek
 */
public class DocumentDTO2KeyValue {
	
	private DocumentDTO2KeyValue() {}
	
	public static List<KeyValue> translate(DocumentDTO docDTO) {
		
		List<KeyValue> kvs = new ArrayList<KeyValue>();
		
		byte[] row = composeRow(docDTO);
		
		kvs.add(composeMetadataFamily(row, docDTO));
		kvs.add(composeContentFamily(row, docDTO));
		
		return kvs;
	}

	private static KeyValue composeContentFamily(byte[] row, DocumentDTO docDTO) {
		
		byte[] family =  Bytes.toBytes(HBaseConstant.FAMILY_CONTENT);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO);
		byte[] value = Bytes.toBytes(docDTO.getMediaConteiner().toString());
		
		return new KeyValue(row,family,qualifier, System.nanoTime(), value);
	}

	private static KeyValue composeMetadataFamily(byte[] row, DocumentDTO docDTO) {
		
		byte[] family =  Bytes.toBytes(HBaseConstant.FAMILY_METADATA);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO);
		byte[] value = Bytes.toBytes(docDTO.getDocumentMetadata().toString());
		
		return new KeyValue(row,family,qualifier, System.nanoTime(), value);
	}

	private static byte[] composeRow(DocumentDTO docDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append(docDTO.getCollection());
//		sb.append(docDTO.getYear());
		if(docDTO.getMediaTypes().size()>0)sb.append(1);
		else sb.append(0);
		sb.append("_");
		sb.append(docDTO.getKey());
		
		return Bytes.toBytes(sb.toString());
	}

}
