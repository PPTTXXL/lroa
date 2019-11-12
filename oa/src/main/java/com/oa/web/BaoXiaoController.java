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
import com.oa.pojo.Baoxiao;
import com.oa.service.BaoxiaoService;
import com.oa.service.EmployeeService;
import com.oa.vo.BaoXiaoVO;
import com.oa.vo.EmployeeVO;

/**
 * @author wl
 * @category 报销表控制器
 *
 */
@Controller
@RequestMapping("/baoXiao")
public class BaoXiaoController {
	Logger log = Logger.getLogger(BaoXiaoController.class);
	@Resource
	private BaoxiaoService baoxiaoService;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * @return 返回所有报销集合列表
	 */
	@RequestMapping("/bxlist")
	public @ResponseBody List<BaoXiaoVO> bxlist(Integer eid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<BaoXiaoVO> bxlist = null;
		EmployeeVO employee = employeeService.findById(eid);
		if (employee.getZid() == 1) {
			bxlist = baoxiaoService.find();
		} else {
			bxlist = baoxiaoService.findlist(employee.getEid());
		}
		return bxlist;
	}

	/**
	 * @param bid
	 *            报销科目编号
	 * @return 根据报销科目编号查询报销对象集合
	 */
	@RequestMapping("/findById")
	public @ResponseBody List<BaoXiaoVO> findById(HttpSession session, Integer bid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		List<BaoXiaoVO> baoxiao = null;
		if (employee.getZid() == 1) {
			baoxiao = baoxiaoService.findById(bid);
		}
		return baoxiao;
	}

	/**
	 * @param uid
	 *            申请人编号
	 * @return 根据申请人编号返回报销集合列表
	 */
	@RequestMapping("/findlist")
	public @ResponseBody List<BaoXiaoVO> findlist(Integer uid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<BaoXiaoVO> baoXiao = baoxiaoService.findlist(uid);
		return baoXiao;
	}

	/**
	 * @param id
	 *            报销编号
	 * @return 根据报销编号返回报销信息
	 */
	@RequestMapping("/findByBid")
	public @ResponseBody BaoXiaoVO findByBid(Integer id) {
		BaoXiaoVO baoXiao = baoxiaoService.findByBid(id);
		return baoXiao;
	}

	/**
	 * @param sid
	 *            状态编号
	 * @return 根据状态编号返回报销集合列表
	 */
	@RequestMapping("/findBySid")
	public @ResponseBody List<BaoXiaoVO> findBySid(HttpSession session, Integer sid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		List<BaoXiaoVO> baoXiao = null;
		if (employee.getZid() == 1) {
			baoXiao = baoxiaoService.findBySid(sid);
		}
		return baoXiao;
	}

	/**
	 * @param baoxiao
	 *            添加一个报销对象
	 * @return
	 */
	@RequestMapping("/addBaoxiao")
	public @ResponseBody Result addBaoxiao(Baoxiao baoxiao, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		boolean flag = baoxiaoService.add(baoxiao);
		Result result = new Result();
		if (flag) {
			result.setResult("添加成功！");
		} else {
			result.setResult("添加失败！");
		}
		return result;
	}

	/**
	 * @param ids
	 *            报销对象编号集合
	 * @return
	 */
	@RequestMapping("/deleteById")
	public @ResponseBody Result deleteById(HttpSession session, int[] ids, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		Result result = new Result();
		boolean flag = baoxiaoService.delete(ids);
		if (flag) {
			result.setResult("删除成功！");
		} else {
			result.setResult("删除失败！");
		}
		return result;
	}

	/**
	 * @param id
	 *            报销对象编号
	 * @param sid
	 *            审核状态编号
	 * @return
	 */
	@RequestMapping("/update")
	public @ResponseBody Result update(HttpSession session, Integer id, int sid, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		Result result = new Result();
		if (employee.getZid() == 1) {
			BaoXiaoVO baoXiaoVO = baoxiaoService.findByBid(id);
			baoXiaoVO.setSid(sid);// 更改审核状态 （同意：sid=2;不同意：sid=3）
			boolean flag = baoxiaoService.update(baoXiaoVO);
			if (flag) {
				result.setResult("更新成功！");
			} else {
				result.setResult("更新失败！");
			}
		}
		return result;
	}
}