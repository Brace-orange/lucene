package one.two.lucenen;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Field;
import org.junit.Test;

import java.awt.*;
import java.io.File;

public class LucenenPra {
    @Test
    public void createIndex() throws Exception {
        Directory dic = FSDirectory.open(new File("D:\\temp\\index").toPath());
        IndexWriter indexwrite = new IndexWriter(dic, new IndexWriterConfig());
        File dir = new File("E:\\BaiduNetdiskDownload\\00 讲义+笔记+资料\\流行框架\\61.会员版(2.0)-就业课(2.0)-Lucene\\87.lucene\\lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for(File f: files) {
            String filename = f.getName();
            String filepath = f.getPath();
            String fileContent = FileUtils.readFileToString(f,"utf-8");
            long filesize = FileUtils.sizeOf(f);
            Field fieldname = new TextField("name", filename, Field.Store.YES);
            Field fieldpath = new TextField("path", filepath, Field.Store.YES);
            Field fieldcontent = new TextField("content", fileContent, Field.Store.YES);
            Field fieldsize = new TextField("size", filesize + "", Field.Store.YES);
            Document document = new Document();
            document.add(fieldname);
            document.add(fieldsize);
            document.add(fieldpath);
            document.add(fieldcontent);
            indexwrite.addDocument(document);
        }
        indexwrite.close();
    }
@Test
    public void searchIndex() throws Exception{
        Directory dic = FSDirectory.open(new File("D:\\temp\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(dic);
        IndexSearcher indexSearch = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("content", "spring"));
        TopDocs topDocs = indexSearch.search(query, 10);
        System.out.println("the max search size " + topDocs.totalHits);
        ScoreDoc[] scoreDoc = topDocs.scoreDocs;
        for(ScoreDoc s: scoreDoc) {
            int docId = s.doc;
            Document document = indexSearch.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));

        }
    }
}
