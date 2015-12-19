package com.mob.lucene.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONObject;
import com.mob.lucene.bean.News;

@Controller
@RequestMapping("/lucene")
public class LuceneController {

    @Autowired(required = false)
    IndexWriter                 indexWriter;
    @Autowired(required = false)
    IKAnalyzer                  ikAnalyzer;

    private static final String STARTTAG = "<font color='red'>";
    private static final String ENDTAG   = "</font>";

    @ResponseBody
    @RequestMapping("listIndexed")
    public String listIndexed() throws CorruptIndexException, IOException {
        IndexSearcher indexSearcher = getSearcher();
        int size = indexWriter.maxDoc();
        Document doc = null;
        News news = null;
        List<News> list = new ArrayList<News>();
        for (int i = 0; i < size; i++) {
            news = new News();
            doc = indexSearcher.doc(i);
            news.setTitle(doc.get("title"));
            news.setUrl(doc.get("url"));
            list.add(news);
        }
        return JSONObject.toJSONString(list);
    }

    @ResponseBody
    @RequestMapping("indexFiles")
    public String indexFiles() throws IOException {
        Directory d = indexWriter.getDirectory();
        String[] fs = d.listAll();
        return JSONObject.toJSONString(fs);
    }

    @ResponseBody
    @RequestMapping("deleteIndexes")
    public String deleteIndexes() {

        String flag = "";

        try {
            indexWriter.deleteAll();
            indexWriter.commit();
            flag = "suc";
        } catch (IOException e) {
            e.printStackTrace();
            try {
                indexWriter.rollback();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            flag = "error";
        }

        return flag;
    }

    @ResponseBody
    @RequestMapping("deleteIndex")
    public String deleteIndex() {

        return null;
    }

    @ResponseBody
    @RequestMapping("updateIndex")
    public String updateIndex() {

        return null;
    }

    @ResponseBody
    @RequestMapping("search")
    public String search(String text) throws ParseException, IOException, InvalidTokenOffsetsException {

        IndexSearcher searcher = getSearcher();

        QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_48, new String[] { "title", "content" },
                                                       ikAnalyzer);

        Query query = parser.parse(text);

        TopDocs td = searcher.search(query, 10);

        ScoreDoc[] sd = td.scoreDocs;

        SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter(STARTTAG, ENDTAG);

        Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));

        Document doc;

        TokenStream tokenStream = null;

        News news = null;
        List<News> list = new ArrayList<News>();

        String title;
        String content;

        for (int i = 0; i < sd.length; i++) {
            news = new News();

            int docId = sd[i].doc;
            doc = searcher.doc(docId);

            title = doc.get("title");
            tokenStream = ikAnalyzer.tokenStream("title", new StringReader(title));
            title = highlighter.getBestFragment(tokenStream, title);
            news.setTitle(title == null ? doc.get("title") : title);

            content = doc.get("content");
            tokenStream = ikAnalyzer.tokenStream("content", new StringReader(content));
            content = highlighter.getBestFragment(tokenStream, content);

            // 正文部分，如果没有匹配的关键字，截取前200个字符
            news.setContent(content == null ? (doc.get("content").length() < 200 ? doc.get("content") : doc.get("content").substring(0,
                                                                                                                                     199)) : content);
            news.setUrl(doc.get("url"));
            news.setDate(doc.get("date"));
            news.setOther1(docId + "");
            list.add(news);

        }
        return JSONObject.toJSONString(list);
    }

    @SuppressWarnings("deprecation")
    private IndexSearcher getSearcher() throws IOException {
        return new IndexSearcher(IndexReader.open(indexWriter.getDirectory()));
    }
}
