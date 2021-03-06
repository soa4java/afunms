package com.afunms.toolService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import com.afunms.toolService.traceroute.TraceRouteExecute;
import com.afunms.toolService.traceroute.TraceRouteManager;

@SuppressWarnings("unchecked")
public class DWRUtil {

	private TraceRouteExecute tre = new TraceRouteExecute();

	private NslookupExecute nslookupExecute = new NslookupExecute();

	public boolean ifExecute(String kind, String ip) {
		// 获得当前的session
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = req.getSession();
		if (kind.equals("ping")) {
			if (pe.readResult(ip, session.getId()) == null) {
				return true;
			}
		}

		if (kind.equals("traceroute")) {
			if (tre.readResult(ip, session.getId()) == null) {
				return true;
			}
		}

		if (kind.equals("nslookup")) {
			if (nslookupExecute.readResult(ip, session.getId()) == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param String
	 *            ip ,int timeout,int maxttl
	 * @return 返回String 此方法为执行TraceRoute命令接口
	 */
	private static StringBuilder tRRsStr = new StringBuilder();

	public void executeTRRsStr(String ip, int timeout, int maxttl) {
		if (ifExecute("traceroute", ip)) {
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
			HttpSession session = req.getSession();
			TraceRouteManager trm = new TraceRouteManager(ip, timeout, maxttl);
			trm.executeTraceRouteResult(session.getId());
		}
	}

	/***************************************************************************
	 * ping命令的参数,n:执行次数 l:包长度（字节）w:超时时间（毫秒）
	 * 
	 * @return 返回String 此方法为执行ping命令接口
	 **************************************************************************/
	private static StringBuilder pingRsStr = new StringBuilder();

	public void executePingRsStr(String ip, int n, int l, int w) {
		if (ifExecute("ping", ip)) {
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
			HttpSession session = req.getSession();
			PingManager pm = new PingManager(ip, n, l, w);
			pm.executePingResult(session.getId());
		}
	}

	/**
	 * @param String
	 *            ip ,int timeout,int maxttl
	 * @return 返回String 此方法为执行nslookup命令接口
	 */
	private static StringBuilder nslookupStr = new StringBuilder();

	public void executeNslookupStr(String domainName) {
		if (ifExecute("nslookup", domainName)) {
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
			HttpSession session = req.getSession();
			NslookupManager trm = new NslookupManager(domainName);
			trm.executeTraceRouteResult(session.getId());
		}
	}

	static PingExecute pe = new PingExecute();

	public String readPingRsStr(String ip) {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = req.getSession();
		pingRsStr.delete(0, pingRsStr.length());
		List resultList = pe.readResult(ip, session.getId());
		if (resultList == null)
			return null;
		for (int i = 0; i < resultList.size(); i++) {
			if (resultList.get(i).toString().trim().length() > 0)
				pingRsStr.append("\r\n" + resultList.get(i));
		}
		return pingRsStr.toString();
	}

	public String readTRRsStr(String ip) {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = req.getSession();
		tRRsStr.delete(0, tRRsStr.length());
		List resultList = tre.readResult(ip, session.getId());
		if (resultList == null) {
			return null;
		}
		for (int i = 0; i < resultList.size(); i++) {
			if (resultList.get(i).toString().trim().length() > 0)
				tRRsStr.append("\r\n" + resultList.get(i));
		}
		return tRRsStr.toString();
	}

	public String readNslookupRsStr(String ip) {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = req.getSession();
		nslookupStr.delete(0, nslookupStr.length());
		List resultList = nslookupExecute.readResult(ip, session.getId());
		if (resultList == null) {
			return null;
		}
		for (int i = 0; i < resultList.size(); i++) {
			if (resultList.get(i).toString().trim().length() > 0)
				nslookupStr.append("\r\n" + resultList.get(i));
		}
		return nslookupStr.toString();
	}

	@SuppressWarnings("static-access")
	public void closePro(String kind, String ip) {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = req.getSession();
		if (kind.trim().equals("traceroute"))
			this.tre.closeTracert(ip, session.getId());
		if (kind.trim().equals("ping"))
			this.pe.closePing(ip, session.getId());
	}
}
