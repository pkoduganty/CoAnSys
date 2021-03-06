/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.citations.indices

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{WritableComparable, Writable, MapFile}
import org.apache.hadoop.fs.Path
import com.nicta.scoobi.core.DList
import com.nicta.scoobi.application.ScoobiConfiguration
import com.nicta.scoobi.Persist._
import com.nicta.scoobi.InputsOutputs._
import scala.Some
import pl.edu.icm.coansys.citations.data.DocumentMetadataWrapper
import pl.edu.icm.coansys.citations.util.hdfs

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
class SimpleIndex[K <: WritableComparable[_] : Manifest, V <: Writable : Manifest](val indexFileUri: String) {
  val conf = new Configuration()
  val path = new java.io.File(new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.DATA_FILE_NAME)).head).getParent
  val indexPathCandidates = new java.io.File(".").listFiles().map(_.getCanonicalPath).filter(_.endsWith(indexFileUri + "/" + MapFile.INDEX_FILE_NAME))
  if (indexPathCandidates.length > 0) {
    val indexPath = new java.io.File(indexPathCandidates.head).getParent
    new java.io.File(indexPath, MapFile.INDEX_FILE_NAME).renameTo(new java.io.File(path, MapFile.INDEX_FILE_NAME))
  }

  val reader = new MapFile.Reader(new Path("file://" + path), conf)

  def get(key: K): Option[V] = {
    val value = manifest[V].erasure.newInstance().asInstanceOf[V]
    val v = reader.get(key, value)
    if (v != null)
      Some(v.asInstanceOf[V])
    else
      None
  }

  def close() {
    if (reader != null)
      reader.close()
  }
}

object SimpleIndex {
  def buildKeyIndex(documents: DList[DocumentMetadataWrapper], indexFile: String)(implicit conf: ScoobiConfiguration) {
    persist(convertToSequenceFile(documents.map(doc => (doc.meta.getKey, doc)), indexFile))
    hdfs.mergeSeqs(indexFile)
    hdfs.convertSeqToMap(indexFile)
  }
}