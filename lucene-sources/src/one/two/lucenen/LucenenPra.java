package one.two.lucenen;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.awt.*;
import java.io.File;

public class LucenenPra {
//    创建索引
    @Test
    public void createIndex() throws Exception {
        Directory dic = FSDirectory.open(new File("D:\\temp\\index").toPath());
//        IndexWriter indexwrite = new IndexWriter(dic, new IndexWriterConfig()); // 使用标准分析器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new IKAnalyzer()); // 使用IK中文分析器
        IndexWriter indexwriter = new IndexWriter(dic, indexWriterConfig);
        File dir = new File("E:\\BaiduNetdiskDownload\\00 讲义+笔记+资料\\流行框架\\61.会员版(2.0)-就业课(2.0)-Lucene\\87.lucene\\lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for(File f: files) {
            String filename = f.getName();
            String filepath = f.getPath();
            String fileContent = FileUtils.readFileToString(f,"utf-8");
            // 域的类型不同
            long filesize = FileUtils.sizeOf(f);
            Field fieldname = new TextField("name", filename, Field.Store.YES);
//            Field fieldpath = new TextField("path", filepath, Field.Store.YES);
            Field fieldpath = new StoredField("path", filepath);
            Field fieldcontent = new TextField("content", fileContent, Field.Store.YES);
//            Field fieldsize = new TextField("size", filesize + "", Field.Store.YES);
            Field fieldsize = new LongPoint("size", filesize);
            Field fieldsizes = new StoredField("size", filesize);
            Document document = new Document();
            document.add(fieldname);
            document.add(fieldsize);
            document.add(fieldpath);
            document.add(fieldcontent);
            document.add(fieldsizes);
            indexwriter.addDocument(document);
        }
        indexwriter.close();
    }

//    查找索引
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
        indexReader.close();
    }

    @Test
//    标准分析器（英文分析器）
    public void testTokenStream() throws Exception {
        Analyzer analyzer = new StandardAnalyzer(); // 英文解析器
        TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model.");
//        TokenStream tokenStream = analyzer.tokenStream("", "自从使用github以来,一直都是在github网站在线上传文件到仓库中,但是有时因为网络或者电脑的原因上传失败。最重要的原因是我习惯本地编辑,完成以后再一起上传github");

        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }

    //IK中文分析器
    @Test
    public void testTokenStreamIK() throws Exception {
        Analyzer analyzer = new IKAnalyzer(); // 中文解析器
//        TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model.");
        TokenStream tokenStream = analyzer.tokenStream("", "自从使用github以来,一直都是在github网站在线上传文件到仓库中,但是有时因为网络或者电脑的原因上传失败。最重要的原因是我习惯本地编辑,完成以后再一起上传github");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }
}
