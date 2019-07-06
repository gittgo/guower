package com.ourslook.guower.utils.office.word.poixwpf;

import junit.framework.TestCase;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @see
 * @author dazer
 * @date 2017/6/20 下午12:00
 */
public class WordBuilderTest extends TestCase {

    public void testDefault() throws Exception {
        //新建一个文档
        XWPFDocument doc = new XWPFDocument();
        //创建一个段落
        XWPFParagraph para = doc.createParagraph();

        //一个XWPFRun代表具有相同属性的一个区域。
        XWPFRun run = para.createRun();
        run.setBold(true); //加粗
        run.setText("加粗的内容");
        run = para.createRun();
        run.setColor("FF0000");
        run.setText("红色的字。");
        OutputStream os = new FileOutputStream("target/simpleWrite.docx");
        //把doc输出到输出流
        doc.write(os);
        os.close();
    }

	// 15 小三
	//  9 小五
	public void testWordBuilder() throws Exception {
		WordBuilder wordBuilder = new WordBuilder();
		// wordBuilder.setDocumentMarginNarrow();
		wordBuilder.configFooter();

		wordBuilder.addText("danny的试卷啊啊啊啊啊", "center", "bold", 15);
		wordBuilder.addText("", "center", null, null);
		wordBuilder.addText("试卷总分：141分   及格分：85分   试卷类型：普通试卷", "center", null, 9);
		wordBuilder.addText("开始时间：2017-04-17 02:04:00   结束时间： 2017-06-30 02:06:00   考试时长：100分钟", "center", null, 9);
		wordBuilder.addText("答卷得分：40分", "center", "bold", 11);
		wordBuilder.addText("", "center", null, null);

		XWPFTable table = wordBuilder.createTable(2, 3);
		wordBuilder.fillTableValue(table, 0, 0, "姓名：chenq");
		wordBuilder.fillTableValue(table, 0, 1, "用户名：chenqq");
		wordBuilder.fillTableValue(table, 0, 2, "证件号：475858258");
		wordBuilder.fillTableValue(table, 1, 0, "开考时间：2017-06-13 17:54:53.0");
		wordBuilder.fillTableValue(table, 1, 1, "交卷时间：2017-06-13 17:55:28.0");
		wordBuilder.fillTableValue(table, 1, 2, "答卷耗时：0分钟");

		wordBuilder.addText("", "left", null, null);
		wordBuilder.addText("我问问 (吾问无为谓无无无无无) 共12题", "left", "bold", 13);
		wordBuilder.addText("", "left", null, null);

		wordBuilder.addText("第1题(21分)：五四运动的直接导火索是（ ）。", "left", null, 9);
		wordBuilder.addText("A：帝国主义对中国的侵略", "left", null, 9);
		wordBuilder.addText("B：北洋军阀政府的对外卖国", "left", null, 9);
		wordBuilder.addText("C：中国在“巴黎和会”上的外交失败", "left", null, 9);
		wordBuilder.addText("【标准答案】C", "left", null, 9);
		wordBuilder.addText("【考生答案】B", "left", null, 9);
		wordBuilder.addText("【试题得分】0分", "left", null, 9);
		wordBuilder.addText("", "left", null, 9);

        OutputStream os = new FileOutputStream("target/word-builder.docx");
		wordBuilder.write(os);
	}

}