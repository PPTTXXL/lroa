package com.oa.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.html.Result;
import com.oa.pojo.Apply;
import com.oa.service.ApplyService;
import com.oa.service.EmployeeService;
import com.oa.vo.ApplyVO;
import com.oa.vo.EmployeeVO;

/**
 * @author cjg
 * @category 考勤控制器
 */
@Controller
@RequestMapping("/kaoQin")
public class KaoQinController {
	public static final Logger log = Logger.getLogger(KaoQinController.class);
	@Resource
	private ApplyService applyService;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/applylist")
	public @ResponseBody List<ApplyVO> applylist(Integer eid, HttpServletResponse response) {
		List<ApplyVO> applylist = null;
		EmployeeVO employee = employeeService.findById(eid);
		if (employee.getZid() == 1) {
			applylist = applyService.findAll();
		} else {
			applylist = applyService.findByUid(employee.getEid());
		}
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Content-type", "application/json");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		return applylist;
	}

	/**
	 * @param id
	 *            请假申请编号
	 * @return 返回根据请假申请编号查询请假申请对象
	 */
	@RequestMapping("/findById")
	public @ResponseBody ApplyVO findById(Integer id) {
		ApplyVO apply = applyService.findById(id);
		return apply;
	}

	/**
	 * @param sid
	 *            请假申请状态编号
	 * @return 返回根据请假申请状态编号查询请假申请
	 */
	@RequestMapping("/findBySid")
	public @ResponseBody List<ApplyVO> findSid(Integer eid, Integer sid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<ApplyVO> applyVOs = null;
		EmployeeVO employee = employeeService.findById(eid);
		if (employee.getZid() == 1) {
			applyVOs = applyService.findBySid(sid);
		}
		return applyVOs;
	}

	/**
	 * @param uid
	 *            员工编号
	 * @return 返回根据员工编号查询到的请假申请对象
	 */
	@RequestMapping("/findByUid")
	public @ResponseBody List<ApplyVO> findByUid(Integer uid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<ApplyVO> applyVOs = applyService.findByUid(uid);
		return applyVOs;
	}

	/**
	 * @param apply
	 *            请假申请对象
	 * @return
	 */
	@RequestMapping("/addApply")
	public @ResponseBody Result addApply(Apply apply, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		apply.setUid(employee.getEid());
		boolean flag = applyService.add(apply);
		Result result = new Result();
		if (flag) {
			result.setResult("添加成功！");
		} else {
			result.setResult("添加失败！");
		}
		return result;
	}

	/**
	 * @param id
	 *            请假申请编号
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Result deleteAppByID(Integer id, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		ApplyVO applyVO = applyService.findById(id);
		Result result = new Result();
		if (applyVO.getUid() == employee.getEid()) {
			boolean flag = applyService.delete(id);
			if (flag) {
				result.setResult("删除成功！");
			} else {
				result.setResult("删除失败！");
			}
		}
		return result;
	}

	/**
	 * @param id
	 *            请假申请编号
	 * @param sid
	 *            审核状态编号
	 * @return
	 */
	@RequestMapping("/update")
	public @ResponseBody Result updateAppByID(Integer eid, Integer id, Integer sid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = employeeService.findById(eid);
		Result result = new Result();
		if (employee.getZid() == 1) {
			ApplyVO applyVO = applyService.findById(id);
			applyVO.setSid(sid);// 更改审核状态 （同意：sid=2;不同意：sid=3）
			boolean flag = applyService.update(applyVO);
			if (flag) {
				result.setResult("更新成功！");
			} else {
				result.setResult("更新失败！");
			}
		}
		return result;
	}

}
