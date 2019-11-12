package com.oa.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oa.html.Result;
import com.oa.pojo.Cg_Apply;
import com.oa.pojo.Property;
import com.oa.service.Cg_ApplyService;
import com.oa.service.PropertyService;
import com.oa.vo.Cg_ApplyVO;
import com.oa.vo.EmployeeVO;

/**
 * @author song
 * @category 资产采购申请控制器
 *
 */
@Controller
@RequestMapping("/caigou")
public class Cg_ApplyController {
	Logger log = Logger.getLogger(Cg_ApplyController.class);
	@Resource
	private Cg_ApplyService cg_ApplyService;
	@Resource
	private PropertyService propertyService;

	// 查询所有资产采购申请信息(已完成)
	@RequestMapping("/Cg_list")
	public @ResponseBody List<Cg_ApplyVO> Cg_list(HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<Cg_ApplyVO> cg_ApplyVOs = cg_ApplyService.findAll();
		return cg_ApplyVOs;
	}

	// 根据采购编号查询采购申请信息(已完成)
	@RequestMapping("/cg_Apply")
	public @ResponseBody Cg_ApplyVO cg_Apply(int pid) {
		Cg_ApplyVO cApply = cg_ApplyService.findById(pid);
		return cApply;
	}

	// 根据库存编号查询采购申请信息(已完成)
	@RequestMapping("/cg_cApply")
	public @ResponseBody List<Cg_ApplyVO> cg_cApply(Integer iid) {
		List<Cg_ApplyVO> cg_Apply = cg_ApplyService.findByCg_ApplyId(iid);
		return cg_Apply;

	}

	// 增加一个资产采购申请表(已完成)
	@RequestMapping("/cg_ApplyAdd")
	public @ResponseBody Result cg_ApplyAdd(Cg_Apply cg_Apply, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		cg_Apply.setUid(employee.getEid());
		boolean flag = cg_ApplyService.add(cg_Apply);
		if (flag) {
			result.setResult("添加成功！");
		} else {
			result.setResult("添加失败！");
		}
		return result;
	}

	// 根据采购编号删除采购申请信息(已完成)
	@RequestMapping("/delete_Cg_Apply")
	public @ResponseBody Result delete_Cg_Apply(Integer pid, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		Cg_ApplyVO applyVO = cg_ApplyService.findById(pid);
		if (employee.getEid() == applyVO.getUid()) {
			boolean flag = cg_ApplyService.deleteByID(pid);
			if (flag) {
				result.setResult("删除成功！");
			} else {
				result.setResult("删除失败！");
			}
		}
		return result;

	}

	// 更新资产采购申请信息(已完成)
	@RequestMapping("/update_Cg_Apply")
	public @ResponseBody Result update_Cg_Apply(Integer pid, Integer sta_id, HttpSession session,
			HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		Result result = new Result();
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		if (employee.getBid() == 4) {
			Cg_ApplyVO cg_ApplyVO = cg_ApplyService.findById(pid);
			cg_ApplyVO.setSta_id(sta_id);// 更改审核状态（同意：sta_id=2；不同意：sta_id=3）
			boolean flag1 = cg_ApplyService.update(cg_ApplyVO);
			if (flag1) {
				if (sta_id == 2) {
					int iid = cg_ApplyVO.getIid();
					Property property = propertyService.findById(iid);
					int a = property.getInumber();
					int i = cg_ApplyVO.getC_number();
					property.setInumber(a + i);
					boolean flag2 = propertyService.update(property);
					if (flag2) {
						result.setResult("更新成功！");
					} else {
						result.setResult("更新失败！");
					}
				} else {
					result.setResult("库存无变化！");
				}
				result.setResult("更新成功！");
			} else {
				result.setResult("更新失败！");
			}
		}
		return result;

	}

}