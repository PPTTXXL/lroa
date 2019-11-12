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
import com.oa.pojo.Destory;
import com.oa.pojo.Property;
import com.oa.service.DestoryService;
import com.oa.service.PropertyService;
import com.oa.vo.DestoryVO;
import com.oa.vo.EmployeeVO;

/**
 * @author Administrator
 * @category 资产报废控制器
 */
@Controller
@RequestMapping("/destory")
public class DestoryContorller {

	Logger log = Logger.getLogger(DestoryContorller.class);

	@Resource
	private DestoryService destoryService;
	@Resource
	private PropertyService propertyService;

	/**
	 * @return 查询所有报废信息
	 */
	@RequestMapping("/bf_listf")
	public @ResponseBody List<DestoryVO> listf(HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		List<DestoryVO> listf = null;
		if (employee.getBid() == 4) {
			listf = destoryService.findAll();
		}
		return listf;

	}

	//
	/**
	 * @param id
	 *            报废编号
	 * @return 根据资产报废编号查询一条资产报废信息
	 */
	@RequestMapping("/bf_select")
	public @ResponseBody DestoryVO select(int id) {
		DestoryVO destoryVO = destoryService.select(id);
		return destoryVO;

	}

	/**
	 * 增加一条报废申请
	 * 
	 * @param destory
	 *            报废申请对象
	 * @return 重定向到查询所有控制器
	 */
	@RequestMapping("/bf")
	public @ResponseBody Result addbf(HttpSession session, Destory destory, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		Result result = new Result();
		if (employee.getBid() == 4) {
			boolean flag1 = destoryService.insert(destory);
			int iid = destory.getIid();
			Property property = propertyService.findById(iid);// 获得该物品库存对象
			int inumber = property.getInumber();// 当前库存该物品总数
			int i = destory.getPcount();// 获得该物品报废数量
			if (flag1) {
				if (inumber >= i) {
					property.setInumber(inumber - i);
					boolean flag2 = propertyService.update(property);
					if (flag2) {
						result.setResult("更新成功！");
					} else {
						result.setResult("更新失败！");
					}
				}
				result.setResult("添加成功！");
			} else {
				result.setResult("添加成功！");
			}
		}
		return result;
	}

	/**
	 * @param ids
	 *            报废编号
	 * @return 重定向到查询所有控制器
	 */
	@RequestMapping("/delete")
	public @ResponseBody Result delete(int ids, HttpSession session, HttpServletResponse response) {
		/** 设置响应头允许ajax跨域访问 **/
		response.setHeader("Access-Control-Allow-Origin", "*");
		EmployeeVO employee = (EmployeeVO) session.getAttribute("employeeVO");
		Result result = new Result();
		if (employee.getBid() == 4) {
			boolean flag = destoryService.delete(ids);
			if (flag) {
				result.setResult("删除成功！");
			} else {
				result.setResult("删除失败！");
			}
		}
		return result;
	}

}
