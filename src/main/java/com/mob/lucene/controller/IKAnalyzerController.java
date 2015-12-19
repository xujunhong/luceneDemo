package com.mob.lucene.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/ik")
public class IKAnalyzerController {

    @ResponseBody
    @RequestMapping(value = "split")
    public String splitWord(String text, String isMaxLength) throws IOException {

        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, isMaxLength.equals("1") ? true : false);

        String splited = "";

        Lexeme lex = null;

        List<String> list = new ArrayList<String>();

        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }

        return JSONObject.toJSONString(list);
    }
}
