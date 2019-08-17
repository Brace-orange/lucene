package one.two.lucenen;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class IndexManager{
    private IndexWriter indexWriter;
    @Before
    public void init() throws Exception{
        indexWriter = new IndexWriter(FSDirectory.open(new File("D:\\temp\\index").toPath()), new IndexWriterConfig(new IKAnalyzer()));
    }
//    添加新文档对象
    @Test
    public void addDocument() throws Exception{
        Document document = new Document();
        document.add(new TextField("name", "hello", Field.Store.YES));
        document.add(new TextField("content", "新添加的文件内容",Field.Store.NO));
        document.add(new StoredField("path", "d:/temp/hello"));
        indexWriter.addDocument(document);
        System.out.println("添加新文档对象");
        indexWriter.close();
    }
// 删除文档
    @Test
    public void deleteAll() throws Exception{
        indexWriter.deleteAll();
        indexWriter.close();
    }

@Test
    public void deleteDocumentByQuery() throws Exception{
        indexWriter.deleteDocuments(new Term("name", "apache"));
        indexWriter.close();
    }

    @Test
    // 修改文档。先删除再添加
    public void updateDocument() throws Exception {
        Document document = new Document();
        document.add(new TextField("name", "更新之后的文档", Field.Store.YES));
        indexWriter.updateDocument(new Term("name", "apache"), document);
        indexWriter.close();
    }
}
