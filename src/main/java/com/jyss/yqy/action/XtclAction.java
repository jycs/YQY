package com.jyss.yqy.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.yqy.entity.BaseConfig;
import com.jyss.yqy.entity.Page;
import com.jyss.yqy.entity.ResponseEntity;
import com.jyss.yqy.entity.Xtcl;
import com.jyss.yqy.service.XtclService;
import com.jyss.yqy.utils.Utils;

@Controller
public class XtclAction {
	@Autowired
	private XtclService clService;

	@RequestMapping("/getClsBy")
	@ResponseBody
	public Page<Xtcl> getClsBy(
			@RequestParam(value = "bz_type", required = false) String bz_type,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "rows", required = true) int rows) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, rows);// 分页语句
		List<Xtcl> clListBy = clService.getClsBy(bz_type, "");
		PageInfo<Xtcl> pageInfoBy = new PageInfo<Xtcl>(clListBy);
		return new Page<Xtcl>(pageInfoBy);
	}

	@RequestMapping("/getClsCo")
	@ResponseBody
	public List<Xtcl> getClsCombox(
			@RequestParam(value = "bz_type", required = true) String bz_type,
			@RequestParam(value = "pid", required = false) String pid) {
		// TODO Auto-generated method stub
		List<Xtcl> clListCo = clService.getClsCombox(bz_type, pid);
		return clListCo;
	}

	@RequestMapping("/addCl")
	@ResponseBody
	public ResponseEntity addDoc(Xtcl cl) {
		// TODO Auto-generated method stub
		int count = 0;
		// if (cl.getId() == 0) {
		// // 洗新增
		// count = clService.addCl(cl);
		// } else {
		// // 修改
		count = clService.updateCl(cl);
		// }

		if (count == 1) {
			return new ResponseEntity("OK", "操作成功！");
		}
		return new ResponseEntity("NO", "操作失败！");
	}

	@RequestMapping("/delCl")
	@ResponseBody
	public ResponseEntity deleteDoc(String strIds) {
		// TODO Auto-generated method stub
		int count = 0;
		List<Long> ids = Utils.stringToLongList(strIds, ",");
		count = clService.deleteCl(ids);
		if (count >= 1) {
			return new ResponseEntity("true", "操作成功!");
		}
		return new ResponseEntity("false", "操作失败！");
	}

	// //////////////b端代理人////////////////////
	@RequestMapping("/b/getSignInfo")
	@ResponseBody
	public Map<String, Object> getBcs() {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		List<BaseConfig> bcList = clService.getBcs("config.signup.b", "");
		if (bcList != null && bcList.size() > 0) {
			m.put("data", bcList.get(0));
			return m;
		}
		m.put("data", "");
		return m;
	}

	@RequestMapping("/b/getDlrInfo")
	@ResponseBody
	public Map<String, Object> getDlrInfo() {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, Object> reMap = new HashMap<String, Object>();
		String info = "";
		// //一盒亚麻籽油对应PV数值
		Xtcl pvcl = clService.getClsValue("pv_type", "1");
		int pvsz = 300;
		if (pvcl != null && pvcl.getBz_value() != null
				&& !pvcl.getBz_value().equals("")) {
			pvsz = Integer.parseInt(pvcl.getBz_value());
		}
		// 初级
		info = (String) getDlcsInfo("1", pvsz, "1", "4").get("info");
		reMap.put("jb1", info);
		reMap.put("jb1Money", getDlcsInfo("1", pvsz, "1", "4").get("money"));
		// 中级
		info = "";
		info = (String) getDlcsInfo("2", pvsz, "2", "5").get("info");
		reMap.put("jb2", info);
		reMap.put("jb2Money", getDlcsInfo("2", pvsz, "2", "5").get("money"));
		// 高级
		info = "";
		info = (String) getDlcsInfo("3", pvsz, "3", "6").get("info");
		reMap.put("jb3", info);
		reMap.put("jb3Money", getDlcsInfo("3", pvsz, "3", "6").get("money"));
		m.put("data", reMap);
		return m;
	}

	// //获取代理参数
	public Map<String, Object> getDlcsInfo(String jbId, int pv, String dyfId,
			String dyhsId) {
		Map<String, Object> mm = new HashMap<String, Object>();
		String info = "";
		if (jbId.equals("1")) {
			info = "初级代理人：投资金额";
		} else if (jbId.equals("2")) {
			info = "中级代理人：投资金额";
		} else if (jbId.equals("3")) {
			info = "高级代理人：投资金额";
		}
		// 代理对应钱
		Xtcl dlf = clService.getClsValue("dyf_type", dyfId);
		int dlfy = 1990;
		if (dlf != null && dlf.getBz_value() != null
				&& !dlf.getBz_value().equals("")) {
			dlfy = Integer.parseInt(dlf.getBz_value());
		}
		info = info + dlfy + "元、";
		// 代理对应盒数
		Xtcl dlhs = clService.getClsValue("dyjf_type", dyhsId);
		int hs = 10;
		if (dlhs != null && dlhs.getBz_value() != null
				&& !dlhs.getBz_value().equals("")) {
			hs = Integer.parseInt(dlhs.getBz_value());
		}
		info = info + "亚麻籽油礼盒" + hs + "盒、对应" + hs * pv + "pv";
		mm.put("money", dlfy);
		mm.put("info", info);
		return mm;
	}
}
