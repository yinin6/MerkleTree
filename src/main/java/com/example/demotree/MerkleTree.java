package com.example.demotree;

import java.util.*;

public class MerkleTree {



    /**
     * 方法一、从下向上构建默克尔树（没有使用）
     * @param dataList 数据
     * @return 根节点
     */
    private static Node buildTree(ArrayList<String> dataList) {
        ArrayList<Node> children = new ArrayList<>();
        for (String message : dataList) {
            children.add(new Node(new Node(null,null,message), null, HashUtil.generateHash(message)));
        }
        ArrayList<Node> parents = new ArrayList<>();
        while (children.size() != 1) {
            int index = 0, length = children.size();
            while (index < length) {
                Node leftChild = children.get(index);
                Node rightChild = null;
                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                } else {
                    rightChild = new Node(null, null, leftChild.getHash());
                }
                String parentHash = HashUtil.generateHash(leftChild.getHash() + rightChild.getHash());
                parents.add(new Node(leftChild, rightChild, parentHash));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }



    /**
     * 层序遍历树（测试用）
     * @param root 二叉树根节点
     */
    private static void printLevelOrderTraversal(Node root) {
        if (root == null) {
            return;
        }

        if ((root.getLeft() == null && root.getRight() == null)) {
            System.out.println(root.getHash());
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                System.out.println(node.getHash());
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }

            if (node != null && node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node != null && node.getRight() != null) {
                queue.add(node.getRight());
            }
        }
    }


    //

    /**
     * 将方法一生成的默克尔树转化为前端展示结构（没有使用）
     * @param root 根节点
     * @param data 前端展示的数据
     */
    public static void traverse(Node root,ShowData data){
        if(root==null){
            return;
        }
        if(root.getLeft()!=null&&root.getRight()==null){
            data.setName(root.getHash().substring(0,6));
            ShowData data1 = new ShowData();
            data1.setName(root.getLeft().getHash());
            data.getChildren().add(data1);
            return;
        }
        data.setName(root.getHash().substring(0,6));
        ShowData data1 = new ShowData();
        ShowData data2 = new ShowData();
        data.getChildren().add(data1);
        data.getChildren().add(data2);
        traverse(root.getLeft(),data1);
        traverse(root.getRight(),data2);
        if(data.getChildren().get(0).getName()==null){
            data.getChildren().clear();
        }
    }




    /**
     * 方法二、从上往下创建树
     * @param dataList 数据
     * @return hash值的数组
     */
    public static String[] buildTreeDfs(ArrayList<String> dataList){
        int nodeNum = dataList.size();
        int realNum =1;
        while(realNum<nodeNum) realNum *=2;
        String realNodes[] = new String[realNum*2];
        dfs(dataList,realNodes,1,0,realNum-1);
        return realNodes;
    }

    //创建树的辅助函数

    /**
     * 方法二构造二叉树的辅助函数
     * @param dataList 数据
     * @param realNodes hash值的数组
     * @param n 索引
     * @param left 左指针
     * @param right 右指针
     */
    private static void dfs(ArrayList<String> dataList,String[] realNodes,int n,int left,int  right){
        if(left>=dataList.size()){
            return;
        }
        if(left==right){
            realNodes[n] =HashUtil.generateHash( dataList.get(left));
            return;
        }
        int mid = (left+right)/2;
        dfs(dataList,realNodes,n*2,left,mid);
        dfs(dataList,realNodes,n*2+1,mid+1,right);
        realNodes[n]= HashUtil.generateHash( realNodes[n*2]+realNodes[n*2+1]);
        //如果不是完全二叉树的处理
        if(realNodes[n*2+1]==null) realNodes[n*2+1] =  realNodes[n*2];
        return;
    }


    /**
     * 方法二得到前端展示数据
     * @param dataList 数据
     * @param realNodes hash数据
     * @return 前端展示数据
     */
    public static ShowData getShowData(ArrayList<String> dataList,String[] realNodes) {
        ShowData parent = new ShowData();
        bfs (dataList,realNodes,parent,1);
        cleanShowData(parent);
        return parent;
    }



    /**
     * 得到展示的数据辅助函数，遍历数据
     * @param dataList 数据
     * @param realNodes hash数据
     * @param root 展示数据的根节点
     * @param n 索引
     */
    public static void bfs(ArrayList<String> dataList,String[] realNodes,ShowData root,int n){
        root.setId(n);
        if(n>realNodes.length-1) {
            int index = (n-realNodes.length)/2;
            if(n%2==0 && index< dataList.size()) root.setName(dataList.get(index));
            return;
        }
        if(realNodes[n]==null) return;
        root.setName(realNodes[n]);
        ShowData left = new ShowData();
        ShowData right = new ShowData();
        root.getChildren().add(left);
        if(n*2+1<realNodes.length) root.getChildren().add(right);
        bfs(dataList,realNodes,left,n*2);
        bfs(dataList,realNodes,right,n*2+1);
        return;
    }

    /**
     * 得到展示的数据辅助函数，去除展示数据中，无用的数据
     * @param root 展示数据的根节点
     */
    public static void cleanShowData(ShowData root){
        if(root==null || root.getChildren().size()==0){
            return;
        }
        remove((ArrayList<ShowData>) root.getChildren());
        if(root.getChildren().size()==0) return;
        for(ShowData s : root.getChildren()){
            cleanShowData(s);
        }
        return;
    }

    /**
     * 得到展示的数据辅助函数，辅助删除迭代函数
     * @param list 展示数据数组
     */
    public static void remove(ArrayList<ShowData> list) {
        Iterator<ShowData> it = list.iterator();
        while (it.hasNext()) {
            ShowData s = it.next();
            if (s.getName()==null) {
                it.remove();
            }
        }
    }

    /**
     * 二分拿到数据索引
     * @param data 需要寻找索引的数据
     * @param dataList 数据
     * @return 索引
     */
    public static int getDataIndex(String data,ArrayList<String> dataList){
        int realNum =1;
        while(realNum<dataList.size()) realNum *=2;
        int left =0;
        int right= dataList.size()-1;
        while (left<=right){
            int mid = left+(right-left)/2;
            if(dataList.get(mid).compareTo(data)>0){
                right=mid-1;
            }else if(dataList.get(mid).compareTo(data)<0){
                left=mid+1;
            }else {
                return mid*2+realNum*2;
            }
        }
        return -1;
    }


    /**
     * 拿到验证不存在数据的索引
     * @param data
     * @param dataList
     * @return
     */
    public static int[] getDataIndexBetween(String data,ArrayList<String> dataList){
        int realNum =1;
        while(realNum<dataList.size()) realNum *=2;
        int left =0;
        int right= dataList.size()-1;
        while (left<=right){
            int mid = left+(right-left)/2;
            if(dataList.get(mid).compareTo(data)>0){
                right=mid-1;
            }else {
                left=mid+1;
            }
        }
        return new int[]{(left-1)*2+realNum*2, (left-1)*2+realNum*2+2};
    }


    /**
     * 验证不存在，需要的所有hash节点的索引
     * @param data
     * @param dataList
     * @return
     */
    public static List<Integer> proveNonExistence(String data,ArrayList<String> dataList){
        ArrayList<Integer> ans= new ArrayList<>();
        int index[] = getDataIndexBetween(data,dataList);
        ans.addAll(proveExistence(index[0]));
        ans.addAll(proveExistence(index[1]));
        return ans;
    }


    /**
     * 计算验证需要的节点索引
     * @param index 验证节点的索引
     * @return hash节点的索引
     */
    public static List<Integer>  proveExistence(int index){
        ArrayList<Integer> ans= new ArrayList<>();
        ans.add(index);
        index= index/2;
        while(index!=1){
            int brother = index %2  ==0 ? index +1 : index -1;
            ans.add(brother);
            index = index % 2 == 0? index / 2 :(index - 1) / 2 ;
        }
        ans.add(1);
        return ans;
    }



    /**
     * 前端显示证明节点，修改颜色
     * @param indexList
     * @param root
     */
    public  static void showExistence(List<Integer> indexList,ShowData root){
        if(root==null){
            return;
        }
        if(indexList.contains(root.getId())){
            root.setColor("#E0F2FE");
        }else {
            root.setColor("");
        }
        for(ShowData s : root.getChildren()){
            showExistence(indexList,s);
        }
        return;
    }

    /**
     * 快排
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(String[] arr,int low,int high){
        int i,j;
        String temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        temp = arr[low];
        while (i<j) {
            while (temp.compareTo(arr[j] )<=0 &&i<j) {
                j--;
            }
            while (temp.compareTo(arr[j] )>0&&i<j) {
                i++;
            }
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        arr[low] = arr[i];
        arr[i] = temp;
        quickSort(arr, low, j-1);
        quickSort(arr, j+1, high);
    }

    /**
     * 比较器
     */
    static class SortStr implements Comparator<String>{
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);    //o1-o2  升序排列
        }
    }

    /**
     * 校验hash,并且展示
     * @param indexList 数据以及hash索引
     * @param realNodes hash节点
     * @param dataList 数据节点
     * @return
     */
    public static ArrayList<ProveData> ProveDataList(List<Integer> indexList,String[] realNodes,ArrayList<String> dataList){
        ArrayList<ProveData> ans = new ArrayList<>();
        for(int i =0;i<indexList.size();i++){
            int index =indexList.get(i);
            if(index==1){//root节点
                ans.add(new ProveData(index,"root",realNodes[1]));
                continue;
            }
            if(index>=realNodes.length){//data节点
                int newIndex = (index- realNodes.length)/2;
                ans.add(new ProveData(index,dataList.get(newIndex),HashUtil.generateHash(dataList.get(newIndex))));
                continue;
            }
            if(index%2==0){//hash节点，左节点
                ans.add(new ProveData(index,realNodes[index],HashUtil.generateHash(realNodes[index]+ans.get(i-1).getChash())));
            }else {//hash节点右节点
                ans.add(new ProveData(index,realNodes[index],HashUtil.generateHash(ans.get(i-1).getChash()+realNodes[index])));
            }
        }
        return ans;//前端展示结果
    }


    public static void main(String[] args) {
        ArrayList<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("标签1");
        dataBlocks.add("标签7");
        dataBlocks.add("标签4");
        dataBlocks.add("标签5");
        dataBlocks.sort(new SortStr());
        int b = getDataIndex("D ",dataBlocks);
        ArrayList<Integer> a= (ArrayList<Integer>) proveNonExistence("标签2",dataBlocks);
        System.out.println(a);
    }
}