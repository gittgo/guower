
package com.ourslook.guower.utils.office.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dazer
 *
 * 使用iText库创建PDF文件
 * http://www.cnblogs.com/chenpi/p/5534595.html
 */
public class PdfRenderer {
	private static Logger logger = LoggerFactory.getLogger(PdfRenderer.class);
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	private boolean debug = false;
	private String processInstanceId;

	public PdfRenderer valueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
		return this;
	}

	public PdfRenderer debug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public PdfRenderer processInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
		return this;
	}

	public void render(String json, OutputStream os) throws Exception {
		this.render(json.getBytes("UTF-8"), os);
	}

	public void render(byte[] bytes, OutputStream os) throws Exception {
		this.render(new ByteArrayInputStream(bytes), os);
	}

	public void render(File file, OutputStream os) throws Exception {
		this.render(new FileInputStream(file), os);
	}

	public void render(Object title, OutputStream os) throws Exception {

		OutputStream out = os;

        Rectangle rectPageSize = new Rectangle(PageSize.A4);
        Document document = new Document(rectPageSize, 50, 50, 50, 50);
        // 将PDF文档写出到out所关联IO设备上的书写对象
        PdfWriter.getInstance(document, out);


        // 添加文档元数据信息
        // document.addTitle(getChinese(title));
        document.addTitle("title");
        document.addSubject("export information");
        document.addAuthor("lingo");
        document.addCreator("lingo");
        document.addKeywords("pdf itext");

        document.open();

		//this.doProcess(document, form);

        document.close(); 
	}
}
