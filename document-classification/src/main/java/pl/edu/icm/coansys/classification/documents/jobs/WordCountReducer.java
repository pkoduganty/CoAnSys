/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordCountReducer extends Reducer<TextArrayWritable, IntWritable, TextArrayWritable, IntWritable> {

    private static Logger logger = LoggerFactory.getLogger(LoggingInDisambiguation.class);
    //protected String reducerId = new Date().getTime() + "_" + new Random().nextFloat();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId + word from this
     * document, V: value 1 (PROCESS) count occurrences of the word (OUT) emits
     * key-value pairs containing K:docId + word from this document, V: the
     * number of word occurrences
     */
    public void reduce(TextArrayWritable key, Iterable<IntWritable> values, Context context) {

        int wc = 0;
        for (Iterator it = values.iterator(); it.hasNext();) {
            wc++;

        }

        try {
            context.write(key, new IntWritable(wc));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
