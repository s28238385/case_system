package ncu.im3069.demo.controller;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Case;
import ncu.im3069.demo.app.CaseHelper;
import ncu.im3069.tools.JsonReader;


@WebServlet("/api/product.do")
public class CaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private CaseHelper ch =  CaseHelper.getHelper();

    public CaseController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        String queryString = URLDecoder.decode(request.getQueryString(), "UTF-8");
        JSONObject jsq = new JSONObject(queryString);
        JsonReader jsr = new JsonReader(request);

        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        int requester_id = jsq.getInt("requester_id");
        int applicant_id = jsq.getInt("applicant_id");
        int case_id = jsq.getInt("case_id");

        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回該案件之資料，否則代表要取回全部資料庫內案件之資料 */
        if (requester_id != 0) {
            JSONObject query = ch.getByRequesterIdAndProgress(requester_id);
            resp.put("status", "200");
            resp.put("message", "該案主案件資料取得成功");
            resp.put("response", query);
        }
        else if (applicant_id != 0) {
            JSONObject query = ch.getByApplicantIdAndProgress(applicant_id);
            resp.put("status", "200");
            resp.put("message", "該接案者資料取得成功");
            resp.put("response", query);
        }
        else if (case_id != 0) {
            JSONObject query = ch.getById(case_id);
            resp.put("status", "200");
            resp.put("message", "該案件資料取得成功");
            resp.put("response", query);
        }
        else {
          JSONObject query = ch.getAll();

            resp.put("status", "200");
            resp.put("message", "所有案件資料取得成功");
            resp.put("response", query);
        }

        jsr.response(resp, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int requester_id = jso.getInt("requester_id");
        String phone = jso.getString("phone");
        String title = jso.getString("title");
        String content = jso.getString("content");
        String area = jso.getString("area");
        String case_time = jso.getString("case_time");
        String end_time = jso.getString("end_time").replace("T", " ");
            
        String pay = jso.getString("pay");

        
        /** 建立一個新的會員物件 */
        Case c = new Case(requester_id, phone, title, content, area, case_time, end_time, pay);
        // int case_id = ch.getCaseId(requester_id, end_time);
        // Progress p = new Progress(case_id, requester_id);
        
        /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
        JSONObject data = ch.create(c);
        // JSONObject pData = ph.create(p);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功更新案件資料");
        resp.put("response", data);
        // resp.put("response", pData);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
        
	}

    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("case_id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = ch.deleteById(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "案件移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    
    /**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int case_id = jso.getInt("case_id");
        int requester_id = jso.getInt("requester_id");
        String phone = jso.getString("phone");
        String title = jso.getString("title");
        String content = jso.getString("content");
        String area = jso.getString("area");
        String case_time = jso.getString("case_time");
        String end_time = jso.getString("end_time").replace("T", " ");
        String pay = jso.getString("pay");

        /** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
        Case c = new Case(case_id, requester_id, phone, title, content, area, case_time, end_time, pay);
        
        /** 透過CaseHelper物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        JSONObject data = ch.update(c);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新案件資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
