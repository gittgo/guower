package com.ourslook.guower.utils.office.word.freemarker;

import com.google.common.collect.Lists;
import com.ourslook.guower.utils.ServletUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import io.swagger.annotations.ApiModelProperty;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dazer
 * @Description: 使用Freemarker导出word
 * @date 2018/3/25 下午3:08
 * <p>
 * 普通格式的word使用poi就可以了，但是复杂的还是要使用模板的，如简历
 */
public class Freemarker2WordTest extends TestCase {   //导出
    private Configuration configuration = null;

    public void test()  throws Exception {

        String path = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "utf-8");

        configuration = new Configuration(new Version("2.3.26"));
        configuration.setDefaultEncoding("UTF-8");
        //模板地址
        String ftlTempleteDir = "/work/android-project/guower/javaCode/guower/src/main/webapp/importTemplate";
        //输出文件地freemarker.core.InvalidReferenceException址
        String outputDir = "/work/android-project/guower/javaCode/guower/target";
        try {
            createWord("1", ftlTempleteDir, outputDir, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出Word文档
     *
     * @throws IOException
     */
    private void createWord(String id, String ftlTempleteDir, String outputDir, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        getData(dataMap, id);
        //configuration.setClassForTemplateLoading(this.getClass(), "/com");  //FTL文件所存在的位置
        try {
            //设置模板地址
            configuration.setDirectoryForTemplateLoading(new File(ftlTempleteDir));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Template t = null;
        try {
            t = configuration.getTemplate("wordFreemarkTemplate1.ftl"); //文件名
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置默认文件名为当前时间：年月日时分秒
//        String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();

        //File outFile = new File(outputDir + fileName + ".doc");
//        OutputStream os = new FileOutputStream("target/simpleWrite.doc");

        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDir+"/aa.doc"), "UTF-8"));
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("target/simpleWrite.docx"), "UTF-8"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            t.process(dataMap, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
        System.out.println("生成word成功...........");
        if (request != null && response != null) {
            InputStream is = new FileInputStream(outputDir + "");
            ServletUtils.setFileDownloadHeader(request, response, "aaaa.doc");
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    /**
     * 导出数据设置
     *
     * @param dataMap
     */
    private void getData(Map<String, Object> dataMap, String id) {
        //资方等信息
        List<BusAgencycibtractVo> list1 = Lists.newArrayList(new BusAgencycibtractVo("张经理", "029-33234444", "户名：张三;银行卡账号:61234324234234;开户银行:浙江支行"));
        List<BusAgencycibtractVo> list2 = Lists.newArrayList(new BusAgencycibtractVo("180m2 上海松江", "14242423421424234242423423421342", "180m2", "未填写", "张三"));//房屋等信息
        dataMap.put("jkInfo1", "借款人名称");
        dataMap.put("jkInfo5", 11 * 10000);
        dataMap.put("jkInfo6", "2");
        dataMap.put("jkInfo9", "张三" + ",银行卡账号:" + "6121412342412341324324234234"
                + ",开户行:" + "浙江杭州支行");
        dataMap.put("jkInfo7", "2018-01-02");
        dataMap.put("jkInfo10", "10%");
        dataMap.put("jkInfo3", "18033334444");
        dataMap.put("jkInfo8", "2019-01-02");
        dataMap.put("userList", list1);
        dataMap.put("userList1", list1);
        dataMap.put("userList2", list2);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 1; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("number", i);
            map.put("content", "内容" + i);
            list.add(map);
        }
        dataMap.put("list", list);
    }

    private class BusAgencycibtractVo {

        private long   id;
        @ApiModelProperty(value="进展状态 ")
        private String bus_AC_STATUS;
        @ApiModelProperty(value="审批备注 ")
        private String bus_AC_REMARK  ;
        @ApiModelProperty(value="创建时间")
        private String bus_AC_CREATETIME ;
        @ApiModelProperty(value="预约公证处 ")
        private String bus_AC_NOTARY_OFFICE ;
        @ApiModelProperty(value="签约机构 ")
        private String bus_AC_CONTRACTOR  ;
        @ApiModelProperty(value="预约签约户名 ")
        private String bus_AC_ACCOUNTNAME ;
        @ApiModelProperty(value="预约签约银行卡号 ")
        private String bus_AC_BANKNUM  ;
        @ApiModelProperty(value="银行开户行（精确至支行） ")
        private String bus_AC_BANKNAME ;
        @ApiModelProperty(value="申请签约日期 ")
        private String bus_AC_CONTRACT_DATE ;
        @ApiModelProperty(value="签约主管 ")
        private long bus_CONTRACT_DIRECTOR ;
        @ApiModelProperty(value="机构签约专员（从终审中冗余，方便后面使用）")
        private long bus_AGENCY_CONTRACT_SP ;
        @ApiModelProperty(value="权证主管 ")
        private long bus_WARRANT_DIRECTOR ;
        @ApiModelProperty(value="权证专员Warrant officer ")
        private long bus_AC_WARRANT_OFFICER ;
        @ApiModelProperty(value="确认领取资料 ")
        private String bus_AC_RECEIVEIMG  ;
        @ApiModelProperty(value="确认领取资料2 ")
        private String bus_AC_RECEIVEIMG2 ;
        @ApiModelProperty(value="确认领取资料2 ")
        private String bus_AC_RECEIVEIMG3 ;
        @ApiModelProperty(value="预约面审表 ")
        private long bus_INT_CODE ;
        @ApiModelProperty(value="面审ID")
        private long bus_INTM_CODE ;
        @ApiModelProperty(value="预约尽调ID ")
        private long bus_CHECK_ID  ;
        @ApiModelProperty(value="尽调ID")
        private long bus_CHM_ID ;
        @ApiModelProperty(value="风控报告 ")
        private long bus_RCR_ID ;
        @ApiModelProperty(value="机构终审ID ")
        private long bus_ORGFJ_ID;
        private int status;
        //案件编号
        private String bus_INTCODE;
        @ApiModelProperty(value="借款金额  ")
        private String bus_RCR_BORMONEY ;
        @ApiModelProperty(value="借款人姓名 ")
        private String bus_RCR_BORROWNAME ;
        @ApiModelProperty(value="借款期限 ")
        private String bus_RCR_DEADLINE ;
        @ApiModelProperty(value="月利率 ")
        private String bus_RCR_MONTHLYRATE ;
        private String real_name;//业务员
        private String real_id;//业务员ID
        private String real_mobile;//业务员联系电话
        private String real_Department;//业务员部门
        private String real_Department_achievement;//业务员业绩
        private String bus_CHM_BORPHONE;//借款人电话
        private String bus_CHM_BORADD;//借款人地址
        private String Create_user;//创建人
        private String bus_rcr_oragin;//机构/个人
        private String bus_rcr_oragin_name;//机构/个人名称
        private String bus_AC_STATUS_NAME;
        private String bus_l_loan_date;
        private String bus_l_id;//放款ID
        private String bus_url_type;//请求类型0：通过 1：拒绝
        private String bus_sign_type;//签约类型
        private String bus_int_department;//贷款部部门
        private String bus_a_id;//数据归档ID
        private String bus_al_borrow_file;//文件名称
        private String bus_al_borrow_type;//借用类型
        private String bus_al_copies;//份数
        private String bus_al_reason;//理由
        private String bus_al_borrow_date;//借用时间
        private String busAlReturnCreateTime;//预计归还时间
        private String bus_al_borrow_file_name;//文件名称
        private String bus_gr_id;//个人签约ID
        private String bus_intcode;//案件编号
        private String bus_p_id;//公示ID
        private String bus_position;//当前登录人的职位
        private String bus_int_applicantman;//贷款部业务员
        private String bus_operation_type;//操作状态：0签约完成
        private String bus_applicantman;//业务员是否完成 0：否 1：是
        private String bus_int_assistant;//风控助理
        private List<BusAgencycibtractVo> jk_busagencycibtractlist;//借款人
        private List<BusAgencycibtractVo> db_busagencycibtractlist;//担保人
        private String bus_bom_telephone;//电话
        private String bus_bom_address;//地址
        private String bus_fkuserId;//担保人/借款人ID
        private String bus_int_applicantman_name = "张三";//理财业务员名称
        private String bus_int_applicantman_department = "风控部";//理财业务员部门
        private String bus_bom_overdueone;//序号
        private String bus_a_rate_comm;//总收佣比例
        private String bus_a_rate_rebate;//总返点比例
        private String bus_a_total_commission;//总收佣金额
        private String bus_a_total_rebate;//总返点金额
        private String bus_a_net_commission;//净收佣金额
        private String bus_ap_achieve;//业绩
        private String bus_outmoney;//出资金额
        private String bus_outmoney_rate;//出资比例
        private String bus_ap_userid;//人员
        private String bus_int_applicantman_id;//业务员ID
        private List<BusAgencycibtractVo> achievementList;//业绩信息
        private String bus_hk_date;//还款日期
        private String bus_zfhm_info;//资方户名信息
        private String bus_fk_fwInfo;//房屋加地址
        private String bus_fk_czbh;//产证编号
        private String bus_fk_mj;//面积
        private String bus_fk_ygjg;//预估价格
        private String bus_fk_syqk;//使用情况
        private String bus_fk_syr;//使用人
        private String bus_ic_contracturl;//文件路径


        public BusAgencycibtractVo(String bus_fk_fwInfo, String bus_fk_czbh, String bus_fk_mj, String bus_fk_syqk, String bus_fk_syr) {
            this.bus_fk_fwInfo = bus_fk_fwInfo;
            this.bus_fk_czbh = bus_fk_czbh;
            this.bus_fk_mj = bus_fk_mj;
            this.bus_fk_syqk = bus_fk_syqk;
            this.bus_fk_syr = bus_fk_syr;
        }

        public BusAgencycibtractVo(String bus_int_applicantman_name, String bus_bom_telephone, String bus_zfhm_info) {
            this.bus_int_applicantman_name = bus_int_applicantman_name;
            this.bus_bom_telephone = bus_bom_telephone;
            this.bus_zfhm_info = bus_zfhm_info;
        }

        public String getBus_fk_fwInfo() {
            return bus_fk_fwInfo;
        }

        public void setBus_fk_fwInfo(String bus_fk_fwInfo) {
            this.bus_fk_fwInfo = bus_fk_fwInfo;
        }

        public String getBus_fk_czbh() {
            return bus_fk_czbh;
        }

        public void setBus_fk_czbh(String bus_fk_czbh) {
            this.bus_fk_czbh = bus_fk_czbh;
        }

        public String getBus_fk_mj() {
            return bus_fk_mj;
        }

        public void setBus_fk_mj(String bus_fk_mj) {
            this.bus_fk_mj = bus_fk_mj;
        }

        public String getBus_fk_syqk() {
            return bus_fk_syqk;
        }

        public void setBus_fk_syqk(String bus_fk_syqk) {
            this.bus_fk_syqk = bus_fk_syqk;
        }

        public String getBus_fk_syr() {
            return bus_fk_syr;
        }

        public void setBus_fk_syr(String bus_fk_syr) {
            this.bus_fk_syr = bus_fk_syr;
        }

        public String getBus_zfhm_info() {
            return bus_zfhm_info;
        }


        public void setBus_zfhm_info(String bus_zfhm_info) {
            this.bus_zfhm_info = bus_zfhm_info;
        }

        public String getBus_int_applicantman_name() {

            return bus_int_applicantman_name;
        }

        public void setBus_int_applicantman_name(String bus_int_applicantman_name) {
            this.bus_int_applicantman_name = bus_int_applicantman_name;
        }

        public String getBus_bom_telephone() {
            return bus_bom_telephone;
        }

        public void setBus_bom_telephone(String bus_bom_telephone) {
            this.bus_bom_telephone = bus_bom_telephone;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getBus_AC_STATUS() {
            return bus_AC_STATUS;
        }

        public void setBus_AC_STATUS(String bus_AC_STATUS) {
            this.bus_AC_STATUS = bus_AC_STATUS;
        }

        public String getBus_AC_REMARK() {
            return bus_AC_REMARK;
        }

        public void setBus_AC_REMARK(String bus_AC_REMARK) {
            this.bus_AC_REMARK = bus_AC_REMARK;
        }

        public String getBus_AC_CREATETIME() {
            return bus_AC_CREATETIME;
        }

        public void setBus_AC_CREATETIME(String bus_AC_CREATETIME) {
            this.bus_AC_CREATETIME = bus_AC_CREATETIME;
        }

        public String getBus_AC_NOTARY_OFFICE() {
            return bus_AC_NOTARY_OFFICE;
        }

        public void setBus_AC_NOTARY_OFFICE(String bus_AC_NOTARY_OFFICE) {
            this.bus_AC_NOTARY_OFFICE = bus_AC_NOTARY_OFFICE;
        }

        public String getBus_AC_CONTRACTOR() {
            return bus_AC_CONTRACTOR;
        }

        public void setBus_AC_CONTRACTOR(String bus_AC_CONTRACTOR) {
            this.bus_AC_CONTRACTOR = bus_AC_CONTRACTOR;
        }

        public String getBus_AC_ACCOUNTNAME() {
            return bus_AC_ACCOUNTNAME;
        }

        public void setBus_AC_ACCOUNTNAME(String bus_AC_ACCOUNTNAME) {
            this.bus_AC_ACCOUNTNAME = bus_AC_ACCOUNTNAME;
        }

        public String getBus_AC_BANKNUM() {
            return bus_AC_BANKNUM;
        }

        public void setBus_AC_BANKNUM(String bus_AC_BANKNUM) {
            this.bus_AC_BANKNUM = bus_AC_BANKNUM;
        }

        public String getBus_AC_BANKNAME() {
            return bus_AC_BANKNAME;
        }

        public void setBus_AC_BANKNAME(String bus_AC_BANKNAME) {
            this.bus_AC_BANKNAME = bus_AC_BANKNAME;
        }

        public String getBus_AC_CONTRACT_DATE() {
            return bus_AC_CONTRACT_DATE;
        }

        public void setBus_AC_CONTRACT_DATE(String bus_AC_CONTRACT_DATE) {
            this.bus_AC_CONTRACT_DATE = bus_AC_CONTRACT_DATE;
        }

        public long getBus_CONTRACT_DIRECTOR() {
            return bus_CONTRACT_DIRECTOR;
        }

        public void setBus_CONTRACT_DIRECTOR(long bus_CONTRACT_DIRECTOR) {
            this.bus_CONTRACT_DIRECTOR = bus_CONTRACT_DIRECTOR;
        }

        public long getBus_AGENCY_CONTRACT_SP() {
            return bus_AGENCY_CONTRACT_SP;
        }

        public void setBus_AGENCY_CONTRACT_SP(long bus_AGENCY_CONTRACT_SP) {
            this.bus_AGENCY_CONTRACT_SP = bus_AGENCY_CONTRACT_SP;
        }

        public long getBus_WARRANT_DIRECTOR() {
            return bus_WARRANT_DIRECTOR;
        }

        public void setBus_WARRANT_DIRECTOR(long bus_WARRANT_DIRECTOR) {
            this.bus_WARRANT_DIRECTOR = bus_WARRANT_DIRECTOR;
        }

        public long getBus_AC_WARRANT_OFFICER() {
            return bus_AC_WARRANT_OFFICER;
        }

        public void setBus_AC_WARRANT_OFFICER(long bus_AC_WARRANT_OFFICER) {
            this.bus_AC_WARRANT_OFFICER = bus_AC_WARRANT_OFFICER;
        }

        public String getBus_AC_RECEIVEIMG() {
            return bus_AC_RECEIVEIMG;
        }

        public void setBus_AC_RECEIVEIMG(String bus_AC_RECEIVEIMG) {
            this.bus_AC_RECEIVEIMG = bus_AC_RECEIVEIMG;
        }

        public String getBus_AC_RECEIVEIMG2() {
            return bus_AC_RECEIVEIMG2;
        }

        public void setBus_AC_RECEIVEIMG2(String bus_AC_RECEIVEIMG2) {
            this.bus_AC_RECEIVEIMG2 = bus_AC_RECEIVEIMG2;
        }

        public String getBus_AC_RECEIVEIMG3() {
            return bus_AC_RECEIVEIMG3;
        }

        public void setBus_AC_RECEIVEIMG3(String bus_AC_RECEIVEIMG3) {
            this.bus_AC_RECEIVEIMG3 = bus_AC_RECEIVEIMG3;
        }

        public long getBus_INT_CODE() {
            return bus_INT_CODE;
        }

        public void setBus_INT_CODE(long bus_INT_CODE) {
            this.bus_INT_CODE = bus_INT_CODE;
        }

        public long getBus_INTM_CODE() {
            return bus_INTM_CODE;
        }

        public void setBus_INTM_CODE(long bus_INTM_CODE) {
            this.bus_INTM_CODE = bus_INTM_CODE;
        }

        public long getBus_CHECK_ID() {
            return bus_CHECK_ID;
        }

        public void setBus_CHECK_ID(long bus_CHECK_ID) {
            this.bus_CHECK_ID = bus_CHECK_ID;
        }

        public long getBus_CHM_ID() {
            return bus_CHM_ID;
        }

        public void setBus_CHM_ID(long bus_CHM_ID) {
            this.bus_CHM_ID = bus_CHM_ID;
        }

        public long getBus_RCR_ID() {
            return bus_RCR_ID;
        }

        public void setBus_RCR_ID(long bus_RCR_ID) {
            this.bus_RCR_ID = bus_RCR_ID;
        }

        public long getBus_ORGFJ_ID() {
            return bus_ORGFJ_ID;
        }

        public void setBus_ORGFJ_ID(long bus_ORGFJ_ID) {
            this.bus_ORGFJ_ID = bus_ORGFJ_ID;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getBus_INTCODE() {
            return bus_INTCODE;
        }

        public void setBus_INTCODE(String bus_INTCODE) {
            this.bus_INTCODE = bus_INTCODE;
        }

        public String getBus_RCR_BORMONEY() {
            return bus_RCR_BORMONEY;
        }

        public void setBus_RCR_BORMONEY(String bus_RCR_BORMONEY) {
            this.bus_RCR_BORMONEY = bus_RCR_BORMONEY;
        }

        public String getBus_RCR_BORROWNAME() {
            return bus_RCR_BORROWNAME;
        }

        public void setBus_RCR_BORROWNAME(String bus_RCR_BORROWNAME) {
            this.bus_RCR_BORROWNAME = bus_RCR_BORROWNAME;
        }

        public String getBus_RCR_DEADLINE() {
            return bus_RCR_DEADLINE;
        }

        public void setBus_RCR_DEADLINE(String bus_RCR_DEADLINE) {
            this.bus_RCR_DEADLINE = bus_RCR_DEADLINE;
        }

        public String getBus_RCR_MONTHLYRATE() {
            return bus_RCR_MONTHLYRATE;
        }

        public void setBus_RCR_MONTHLYRATE(String bus_RCR_MONTHLYRATE) {
            this.bus_RCR_MONTHLYRATE = bus_RCR_MONTHLYRATE;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getReal_id() {
            return real_id;
        }

        public void setReal_id(String real_id) {
            this.real_id = real_id;
        }

        public String getReal_mobile() {
            return real_mobile;
        }

        public void setReal_mobile(String real_mobile) {
            this.real_mobile = real_mobile;
        }

        public String getReal_Department() {
            return real_Department;
        }

        public void setReal_Department(String real_Department) {
            this.real_Department = real_Department;
        }

        public String getReal_Department_achievement() {
            return real_Department_achievement;
        }

        public void setReal_Department_achievement(String real_Department_achievement) {
            this.real_Department_achievement = real_Department_achievement;
        }

        public String getBus_CHM_BORPHONE() {
            return bus_CHM_BORPHONE;
        }

        public void setBus_CHM_BORPHONE(String bus_CHM_BORPHONE) {
            this.bus_CHM_BORPHONE = bus_CHM_BORPHONE;
        }

        public String getBus_CHM_BORADD() {
            return bus_CHM_BORADD;
        }

        public void setBus_CHM_BORADD(String bus_CHM_BORADD) {
            this.bus_CHM_BORADD = bus_CHM_BORADD;
        }

        public String getCreate_user() {
            return Create_user;
        }

        public void setCreate_user(String create_user) {
            Create_user = create_user;
        }

        public String getBus_rcr_oragin() {
            return bus_rcr_oragin;
        }

        public void setBus_rcr_oragin(String bus_rcr_oragin) {
            this.bus_rcr_oragin = bus_rcr_oragin;
        }

        public String getBus_rcr_oragin_name() {
            return bus_rcr_oragin_name;
        }

        public void setBus_rcr_oragin_name(String bus_rcr_oragin_name) {
            this.bus_rcr_oragin_name = bus_rcr_oragin_name;
        }

        public String getBus_AC_STATUS_NAME() {
            return bus_AC_STATUS_NAME;
        }

        public void setBus_AC_STATUS_NAME(String bus_AC_STATUS_NAME) {
            this.bus_AC_STATUS_NAME = bus_AC_STATUS_NAME;
        }

        public String getBus_l_loan_date() {
            return bus_l_loan_date;
        }

        public void setBus_l_loan_date(String bus_l_loan_date) {
            this.bus_l_loan_date = bus_l_loan_date;
        }

        public String getBus_l_id() {
            return bus_l_id;
        }

        public void setBus_l_id(String bus_l_id) {
            this.bus_l_id = bus_l_id;
        }

        public String getBus_url_type() {
            return bus_url_type;
        }

        public void setBus_url_type(String bus_url_type) {
            this.bus_url_type = bus_url_type;
        }

        public String getBus_sign_type() {
            return bus_sign_type;
        }

        public void setBus_sign_type(String bus_sign_type) {
            this.bus_sign_type = bus_sign_type;
        }

        public String getBus_int_department() {
            return bus_int_department;
        }

        public void setBus_int_department(String bus_int_department) {
            this.bus_int_department = bus_int_department;
        }

        public String getBus_a_id() {
            return bus_a_id;
        }

        public void setBus_a_id(String bus_a_id) {
            this.bus_a_id = bus_a_id;
        }

        public String getBus_al_borrow_file() {
            return bus_al_borrow_file;
        }

        public void setBus_al_borrow_file(String bus_al_borrow_file) {
            this.bus_al_borrow_file = bus_al_borrow_file;
        }

        public String getBus_al_borrow_type() {
            return bus_al_borrow_type;
        }

        public void setBus_al_borrow_type(String bus_al_borrow_type) {
            this.bus_al_borrow_type = bus_al_borrow_type;
        }

        public String getBus_al_copies() {
            return bus_al_copies;
        }

        public void setBus_al_copies(String bus_al_copies) {
            this.bus_al_copies = bus_al_copies;
        }

        public String getBus_al_reason() {
            return bus_al_reason;
        }

        public void setBus_al_reason(String bus_al_reason) {
            this.bus_al_reason = bus_al_reason;
        }

        public String getBus_al_borrow_date() {
            return bus_al_borrow_date;
        }

        public void setBus_al_borrow_date(String bus_al_borrow_date) {
            this.bus_al_borrow_date = bus_al_borrow_date;
        }

        public String getBusAlReturnCreateTime() {
            return busAlReturnCreateTime;
        }

        public void setBusAlReturnCreateTime(String busAlReturnCreateTime) {
            this.busAlReturnCreateTime = busAlReturnCreateTime;
        }

        public String getBus_al_borrow_file_name() {
            return bus_al_borrow_file_name;
        }

        public void setBus_al_borrow_file_name(String bus_al_borrow_file_name) {
            this.bus_al_borrow_file_name = bus_al_borrow_file_name;
        }

        public String getBus_gr_id() {
            return bus_gr_id;
        }

        public void setBus_gr_id(String bus_gr_id) {
            this.bus_gr_id = bus_gr_id;
        }

        public String getBus_intcode() {
            return bus_intcode;
        }

        public void setBus_intcode(String bus_intcode) {
            this.bus_intcode = bus_intcode;
        }

        public String getBus_p_id() {
            return bus_p_id;
        }

        public void setBus_p_id(String bus_p_id) {
            this.bus_p_id = bus_p_id;
        }

        public String getBus_position() {
            return bus_position;
        }

        public void setBus_position(String bus_position) {
            this.bus_position = bus_position;
        }

        public String getBus_int_applicantman() {
            return bus_int_applicantman;
        }

        public void setBus_int_applicantman(String bus_int_applicantman) {
            this.bus_int_applicantman = bus_int_applicantman;
        }

        public String getBus_operation_type() {
            return bus_operation_type;
        }

        public void setBus_operation_type(String bus_operation_type) {
            this.bus_operation_type = bus_operation_type;
        }

        public String getBus_applicantman() {
            return bus_applicantman;
        }

        public void setBus_applicantman(String bus_applicantman) {
            this.bus_applicantman = bus_applicantman;
        }

        public String getBus_int_assistant() {
            return bus_int_assistant;
        }

        public void setBus_int_assistant(String bus_int_assistant) {
            this.bus_int_assistant = bus_int_assistant;
        }

        public List<BusAgencycibtractVo> getJk_busagencycibtractlist() {
            return jk_busagencycibtractlist;
        }

        public void setJk_busagencycibtractlist(List<BusAgencycibtractVo> jk_busagencycibtractlist) {
            this.jk_busagencycibtractlist = jk_busagencycibtractlist;
        }

        public List<BusAgencycibtractVo> getDb_busagencycibtractlist() {
            return db_busagencycibtractlist;
        }

        public void setDb_busagencycibtractlist(List<BusAgencycibtractVo> db_busagencycibtractlist) {
            this.db_busagencycibtractlist = db_busagencycibtractlist;
        }

        public String getBus_bom_address() {
            return bus_bom_address;
        }

        public void setBus_bom_address(String bus_bom_address) {
            this.bus_bom_address = bus_bom_address;
        }

        public String getBus_fkuserId() {
            return bus_fkuserId;
        }

        public void setBus_fkuserId(String bus_fkuserId) {
            this.bus_fkuserId = bus_fkuserId;
        }

        public String getBus_int_applicantman_department() {
            return bus_int_applicantman_department;
        }

        public void setBus_int_applicantman_department(String bus_int_applicantman_department) {
            this.bus_int_applicantman_department = bus_int_applicantman_department;
        }

        public String getBus_bom_overdueone() {
            return bus_bom_overdueone;
        }

        public void setBus_bom_overdueone(String bus_bom_overdueone) {
            this.bus_bom_overdueone = bus_bom_overdueone;
        }

        public String getBus_a_rate_comm() {
            return bus_a_rate_comm;
        }

        public void setBus_a_rate_comm(String bus_a_rate_comm) {
            this.bus_a_rate_comm = bus_a_rate_comm;
        }

        public String getBus_a_rate_rebate() {
            return bus_a_rate_rebate;
        }

        public void setBus_a_rate_rebate(String bus_a_rate_rebate) {
            this.bus_a_rate_rebate = bus_a_rate_rebate;
        }

        public String getBus_a_total_commission() {
            return bus_a_total_commission;
        }

        public void setBus_a_total_commission(String bus_a_total_commission) {
            this.bus_a_total_commission = bus_a_total_commission;
        }

        public String getBus_a_total_rebate() {
            return bus_a_total_rebate;
        }

        public void setBus_a_total_rebate(String bus_a_total_rebate) {
            this.bus_a_total_rebate = bus_a_total_rebate;
        }

        public String getBus_a_net_commission() {
            return bus_a_net_commission;
        }

        public void setBus_a_net_commission(String bus_a_net_commission) {
            this.bus_a_net_commission = bus_a_net_commission;
        }

        public String getBus_ap_achieve() {
            return bus_ap_achieve;
        }

        public void setBus_ap_achieve(String bus_ap_achieve) {
            this.bus_ap_achieve = bus_ap_achieve;
        }

        public String getBus_outmoney() {
            return bus_outmoney;
        }

        public void setBus_outmoney(String bus_outmoney) {
            this.bus_outmoney = bus_outmoney;
        }

        public String getBus_outmoney_rate() {
            return bus_outmoney_rate;
        }

        public void setBus_outmoney_rate(String bus_outmoney_rate) {
            this.bus_outmoney_rate = bus_outmoney_rate;
        }

        public String getBus_ap_userid() {
            return bus_ap_userid;
        }

        public void setBus_ap_userid(String bus_ap_userid) {
            this.bus_ap_userid = bus_ap_userid;
        }

        public String getBus_int_applicantman_id() {
            return bus_int_applicantman_id;
        }

        public void setBus_int_applicantman_id(String bus_int_applicantman_id) {
            this.bus_int_applicantman_id = bus_int_applicantman_id;
        }

        public List<BusAgencycibtractVo> getAchievementList() {
            return achievementList;
        }

        public void setAchievementList(List<BusAgencycibtractVo> achievementList) {
            this.achievementList = achievementList;
        }

        public String getBus_hk_date() {
            return bus_hk_date;
        }

        public void setBus_hk_date(String bus_hk_date) {
            this.bus_hk_date = bus_hk_date;
        }

        public String getBus_fk_ygjg() {
            return bus_fk_ygjg;
        }

        public void setBus_fk_ygjg(String bus_fk_ygjg) {
            this.bus_fk_ygjg = bus_fk_ygjg;
        }

        public String getBus_ic_contracturl() {
            return bus_ic_contracturl;
        }

        public void setBus_ic_contracturl(String bus_ic_contracturl) {
            this.bus_ic_contracturl = bus_ic_contracturl;
        }
    }
}
