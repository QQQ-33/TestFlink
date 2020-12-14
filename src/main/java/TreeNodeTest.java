import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class TreeNodeTest {
    public static void main(String[] args) throws Exception{
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"aaa","bbb","111"});
        list.add(new String[]{"aaa","bbb",null});
        list.add(new String[]{"aaa","ccc","111"});
        list.add(new String[]{"fff","ccc","222"});
        TreeNode tree = new TreeNode();
        tree.setChildren(new ArrayList<>());
        for(String[] arr: list){
            putNode(tree, arr, 0);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tree);
        System.out.println(json);

    }
    public static void putNode(TreeNode node, String[] arr, int i){
        if(arr[i] == null){
            return ;
        } else if(i == arr.length - 1){
            TreeNode tmp = new TreeNode(arr[i], null);
            node.getChildren().add(tmp);
            return ;
        }
        if(node.getName() == null){
            node.setName(arr[i]);
        }
        List<TreeNode> childes = node.getChildren();
        boolean isHave = false;
        for(TreeNode n: childes){
            if(n.getName().equals(arr[i])){
                isHave = true;
                putNode(n, arr, i + 1);
            }
        }
        if(!isHave) {
            TreeNode tmp = new TreeNode(arr[i], new ArrayList<>());
            node.getChildren().add(tmp);
            putNode(tmp, arr, i + 1);
        }
    }


}
class TreeNode {
    private String name;
    private List<TreeNode> children;
    TreeNode(){}
    TreeNode (String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}

