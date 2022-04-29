package com.example.demotree;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.demotree.MerkleTree.*;


@RestController
public class Controller {
    @RequestMapping("/in")
    public String sayHello() {
        return "index";
    }


    ShowData data = null;
    ArrayList<String> dataList = null;
    String realNodes[] = null;
    List<Integer> indexList =null;

    @RequestMapping("/data")
    public List<ShowData> ChangeData(@RequestBody Entity e){
        dataList = (ArrayList<String>) e.list;
        dataList.sort(new SortStr());
        realNodes = buildTreeDfs(dataList);
        data = getShowData((ArrayList<String>) e.list,realNodes);
        List<ShowData> ans = new ArrayList<>();
        ans.add(data);
        return ans;
    }

    //验证存在
    @RequestMapping("/prove")
    public List<ShowData> ProveData(@RequestBody Form e){
        if(e.getIsExist()==false){
            indexList = proveNonExistence(e.getData(),dataList);
        }else {
            if(e.getChoice().equals("方式2")){
                int index = getDataIndex(e.getData(),dataList);
                indexList = proveExistence(index);
            }else {
                indexList = proveExistence(e.getIndex());
            }
        }
        //展示数据
        showExistence(indexList,data);
        List<ShowData> ans = new ArrayList<>();
        ans.add(data);
        return ans;
    }

    @RequestMapping("/detailed")
    public ArrayList<ProveData> getProveData(){
        ArrayList<ProveData> proveData= ProveDataList(indexList,realNodes,dataList);
        return proveData;
    }


}
