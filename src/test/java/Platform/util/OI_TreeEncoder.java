package Platform.util;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Platform.DashConsole.OV_Issue;

public abstract class OI_TreeEncoder {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	abstract MutableTreeNode decodeToTree(String userObject);

	abstract Object encodeFreomTree(DefaultMutableTreeNode node);

	public List<OV_Issue> listAsTree(List<OV_Issue> list, MutableTreeNode parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
			list.add((OV_Issue) node.getUserObject());
			listAsTree(list, node);
		}
		return null;
	}

}