package Platform.DashConsole;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.barolab.html.HmTR;
import com.barolab.html.HttpPrintStream;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.SavedQuery;

import lombok.extern.java.Log;

@Log
public class Report4PnList extends MainDashBoard {

	HttpPrintStream h;
	List<OV_Issue> nlist;

	// 이름순 소트 : 고객편의사항
	// 상태별 소트
	public void test() {
		login();
		List<SavedQuery> savedQueries;
		try {

			List<Issue> list = redmine.getIssueManager().getIssues("vepg-si-pr", 160);// proj, query
			nlist = OV_Issue.toList(list);
			log.info("load pr count=" + list.size());

			Ascending ascending = new Ascending();
			Collections.sort(nlist, ascending);

		} catch (RedmineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File fp = new File("C:/tmp/test.html");
		try {
			HttpPrintStream h = new HttpPrintStream(fp);

			int count = 1;
			h.println("<table class='mytable' >");
			for (OV_Issue issue : nlist) {

				if (match(issue.getStatus(), "Resolved", "Closed", "Feedback")) {
					continue;

				}
				if (contains(issue.getSubject(), "(현상대기)", "R140예비", "R140검토")) {
					continue;
				}
				log.info("#" + (count));

				log.info(issue.getSubject() + " ==> " + issue.getAssignee());

				// h.println("<tr>");

				HmTR tr = new HmTR();

				String url = "http://redmine.ericssonlg.com/redmine/issues/" + issue.getId();

				String ref = mkHref("<a href=%s %s >%d</a>", qt2(url), " target=" + qt1("_sub"), issue.getId());
				tr.addTD().setWidth(50).setAligh("center").add(count);
				tr.addTD().setWidth(120).setAligh("center").add(ref);
				tr.addTD().setWidth(120).add(issue.getStatus());
				tr.addTD().add(issue.getSubject());
				tr.addTD().setWidth(150).add(issue.getAssignee());
				tr.toHTML(h);
				count++;
			}
			h.println("</table>");
			h.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public String mkHref(String msg, Object... args) {
		String s = String.format(msg, args);
		System.out.println(s);
		return s;

	}

	public String qt1(String msg) {
		return "'" + msg + "'";
	}

	public String qt2(String msg) {
		return "\"" + msg + "\"";
	}

	public boolean match(String key, String... args) {
		for (String arg : args) {
			if (key.equals(arg)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(String key, String... args) {
		for (String arg : args) {
			if (key.indexOf(arg) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		new Report4PnList().test();
	}

	private class Ascending implements Comparator<OV_Issue> {

		@Override
		public int compare(OV_Issue arg0, OV_Issue arg1) {
			return arg0.getAssignee().compareTo(arg1.getAssignee());
		}

	}
}