
package com.ourslook.guower.utils.office.word.poixwpf;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * apache poi 也支持 word 处理 org.apache.poi
 * 类模板：BusIndividualcontractServiceImpl
 */
public class WordBuilder {
    private XWPFDocument doc = new XWPFDocument();

    public void configPageMargin() {
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        // CTPageMar pageMar = sectPr.addNewPgMar();
        // pageMar.setLeft(BigInteger.valueOf(720L));
        // pageMar.setTop(BigInteger.valueOf(1440L));
        // pageMar.setRight(BigInteger.valueOf(720L));
        // pageMar.setBottom(BigInteger.valueOf(1440L));
    }

    public void addText(String text, String align, String bold, Integer fontSize) {
        XWPFParagraph para = doc.createParagraph();
        if ("center".equals(align)) {
            para.setAlignment(ParagraphAlignment.CENTER);
        }
        XWPFRun run = para.createRun();
        if ("bold".equals(bold)) {
            run.setBold(true);
        }
        if (fontSize != null) {
            run.setFontSize(fontSize);
        }
        run.setText(text);
    }

    public XWPFTable createTable(int rowSize, int cellSize) {
        XWPFTable table = doc.createTable(rowSize, cellSize);
        this.configTableWidth(table);
        this.configTableBorderTop(table);

        return table;
    }

    public void configTableWidth(XWPFTable table) {
        //表格属性
        CTTblPr tablePr = table.getCTTbl().addNewTblPr();
        //表格宽度
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(8000));
        //width.setType(STTblWidth.PCT);
        // tablePr.setTblW(width);
        // table.setTblPr(tablePr);
    }

    public void configTableBorderTop(XWPFTable table) {
        CTTblPr tablePr = table.getCTTbl().addNewTblPr();
        CTTblBorders tblBorders = tablePr.isSetTblBorders()
                ? tablePr.getTblBorders()
                : tablePr.addNewTblBorders();
        CTBorder borderTop = tblBorders.addNewTop();
        borderTop.setVal(STBorder.SINGLE);
        borderTop.setSz(new BigInteger("3"));
        borderTop.setColor("000000");
        CTBorder borderBottom = tblBorders.addNewBottom();
        borderBottom.setVal(STBorder.NONE);

        tblBorders.setTop(borderTop);
        tblBorders.setBottom(borderBottom);
        tblBorders.setLeft(borderBottom);
        tblBorders.setRight(borderBottom);
        tblBorders.setInsideV(borderBottom);
    }

    public void configTableBorderNone(XWPFTable table) {
        CTTblPr tablePr = table.getCTTbl().addNewTblPr();
        CTTblBorders tblBorders = tablePr.isSetTblBorders()
                ? tablePr.getTblBorders()
                : tablePr.addNewTblBorders();
        CTBorder borderTop = tblBorders.addNewTop();
        borderTop.setVal(STBorder.NONE);

        tblBorders.setTop(borderTop);
        tblBorders.setBottom(borderTop);
        tblBorders.setLeft(borderTop);
        tblBorders.setRight(borderTop);
        tblBorders.setInsideV(borderTop);
    }

    public void fillTableValue(XWPFTable table, int rowIndex, int colIndex, String text) {
        XWPFTableRow row = table.getRow(rowIndex);
        XWPFTableCell cell = row.getCell(colIndex);

        CTTc cttc = cell.getCTTc();
        // CTTblPr tablePr = cttc.addNewTblPr();

        XWPFParagraph para = cell.getParagraphs().get(0);
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setFontSize(9);
        run.setText(text);
    }

    public void configFooter() throws Exception {
        CTP ctp = CTP.Factory.newInstance();

        XWPFParagraph paraLeft = new XWPFParagraph(ctp, doc);
        XWPFRun runLeft = paraLeft.createRun();
        runLeft.setText("生成日期：" + "2017-06-17 00:00:00");

        XWPFParagraph paraRight = new XWPFParagraph(ctp, doc);
        XWPFRun runRight = paraRight.createRun();
        runRight.setText("第");

        runRight = paraRight.createRun();
        CTFldChar fldChar = runRight.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.BEGIN);

        runRight = paraRight.createRun();
        CTText ctText = runRight.getCTR().addNewInstrText();
        ctText.setStringValue("PAGE  \\* MERGEFORMAT");
        ctText.setSpace(SpaceAttribute.Space.PRESERVE);

        fldChar = runRight.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.END);

        runRight = paraRight.createRun();
        runRight.setText("页");

        XWPFParagraph[] newparagraphs = new XWPFParagraph[2];
        newparagraphs[0] = paraLeft;
        newparagraphs[1] = paraRight;
        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(
                doc, sectPr);
        XWPFFooter footer = headerFooterPolicy.createFooter(STHdrFtr.DEFAULT);
        XWPFTable table = footer.createTable(1, 2);
        this.configTableWidth(table);
        this.configTableBorderNone(table);

        XWPFTableRow row = table.getRow(0);
        XWPFTableCell cell = row.getCell(0);
        XWPFParagraph para = cell.getParagraphs().get(0);
        XWPFRun run = para.createRun();
        run.setText("生成日期：" + "2017-06-17 00:00:00");

        cell = row.getCell(1);
        para = cell.getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.RIGHT);
        run = para.createRun();
        run.setText("第");

        run = para.createRun();
        fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.BEGIN);

        run = para.createRun();
        ctText = run.getCTR().addNewInstrText();
        ctText.setStringValue("PAGE  \\* MERGEFORMAT");
        ctText.setSpace(SpaceAttribute.Space.PRESERVE);

        fldChar = run.getCTR().addNewFldChar();
        fldChar.setFldCharType(STFldCharType.END);

        run = para.createRun();
        run.setText("页");
    }

    public void write(OutputStream os) throws IOException {
        doc.write(os);
        os.flush();
        os.close();
    }
}
