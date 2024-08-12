package org.jeecg.modules.coderQ.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫部分
 */
public class Spider {
    final private String BASE_URL = "https://m.weibo.cn/comments/hotflow?id=%s&mid=%s&max_id_type=0";
    final private String CHILD_URL = "https://m.weibo.cn/comments/hotFlowChild?cid=%s&max_id_type=0";
    // 子部分
    private String strCookie;
    private String strAddress;


    public static void main(String[] args) {

        new Thread(() -> {
            String cookie = "WEIBOCN_FROM=1110006030; loginScene=102003; SUB=_2A25JXyacDeRhGeFJ7lYT-CzKyTyIHXVqoErUrDV6PUJbkdANLWSkkW1Nf7E7v2kF0KFrKIkO25yqz4XcrxSO2IDu; _T_WM=51575951013; XSRF-TOKEN=f603c6; MLOGIN=1; M_WEIBOCN_PARAMS=oid%3D4899563750033845%26luicode%3D10000011%26lfid%3D231583%26uicode%3D10000011%26fid%3D102803";
            String id = "4899848640006952";
            new Spider(cookie, id).process();
        }).start();
    }


    public Spider(String strCookie, String strAddress) {
        this.strCookie = strCookie;
        this.strAddress = strAddress;
    }

    // 爬虫处理
    public List<Comment> process() {
        System.out.println("处理开始...");
        try {
            // 查询数据并输出
            List<Comment> list = getBasePageComment();
            handleData(list);
            list.forEach(System.out::println);
            System.out.println("size------------" + list.size());
            System.out.println("处理结束...");
            return list;
        } catch (Exception e) {
        }
        return null;
    }

    // 取得文章基本评论
    private List<Comment> getBasePageComment() throws InterruptedException {
        List<Comment> listRet = new ArrayList<>();
        Long maxId = 0L;
        while (1 == 1) {
            // 基本请求处理
            JSONObject jsonBase = htmlRequest(BASE_URL, strAddress, maxId);
            if (null == jsonBase) {
                break;
            }
            // 结果处理
            JSONObject jsonData = jsonBase.getJSONObject("data");
            maxId = jsonData.getLong("max_id"); // 最大id
            // 结果的评论信息
            JSONArray arrayComment = jsonData.getJSONArray("data");
            if (!arrayComment.isEmpty()) {
                List<Comment> listComment = arrayComment.toList(Comment.class);
//                getSubComment(listComment);
                listRet.addAll(listComment);
            }
            // 判断是否到最后
            if (arrayComment.isEmpty() || maxId == 0L) {
                break;
            }
        }
        return listRet;
    }

    // 子评论
    private void getSubComment(List<Comment> listComment) throws InterruptedException {
        Long maxId = 0L;
        for (Comment comment : listComment) {
            if (comment.getTotal_number() > 0) {
                // 子请求
                JSONObject jsonBase = htmlRequest(CHILD_URL, comment.getId(), maxId);
                if (null == jsonBase) {
                    continue;
                }
                // 结果处理
                maxId = jsonBase.getLong("max_id"); // 最大id
                // 结果的评论信息
                JSONArray arrayComment = jsonBase.getJSONArray("data");
                if (!arrayComment.isEmpty()) {
                    List<Comment> listSubComment = arrayComment.toList(Comment.class);
                    comment.setList(listSubComment);
                }
            }
        }
    }


    /**
     * 网络请求
     */
    private JSONObject htmlRequest(String inUrl, String inAddress, Long inMaxId) throws InterruptedException {
        String url = String.format(inUrl, inAddress, inAddress);
        if (null != inMaxId && 0L != inMaxId) {
            url = url + "&max_id=" + inMaxId;
        }
        System.out.println("请求链接：" + url);
        for (int i = 1; i <= 3; i++) {
            // 请求


            String retBase = HttpRequest.post(url)
                    .header(Header.COOKIE, strCookie) // cookie处理
                    .timeout(30000)//超时，毫秒
                    .execute().body();

            if (StrUtil.isBlank(retBase) || !JSONUtil.isJsonObj(retBase)) {
                Thread.sleep(1000);
                continue;
            }
            JSONObject jsonBase = JSONUtil.parseObj(retBase);
            if (jsonBase.getInt("ok") != 1) {
                Thread.sleep(1000);
                continue;
            }
            return jsonBase;
        }
        return null;
    }

    /**
     * 数据处理
     */
    private void handleData(List<Comment> list) {
        for (Comment comment : list) {
            // 去除标签
            comment.setText(Html.delHTMLTag(comment.getText()));
        }
    }

}
