package org.jeecg.modules.coderQ.util;

import lombok.Data;

import java.util.List;

/**
 * 评论
 */
@Data
public class Comment {
    private String floor_number; // 层数
    private String rootid; // 根id
    private String id;
//    private String max_id;
//    private String bid; // 用户id
    private Integer total_number; // 子评论数量
//    private String comments; // 子评论简要
    private String like_count; // 点赞数
//    private String disable_reply; // 禁止回答
//    private String created_at; // 创建时间
//    private String mid; // 对象id
//    private String source;
//    private String rootidstr;
//    private String isLikedByMblogAutho;
//    private String liked;
//    private String user; // 评论用户信息
//    private String more_info_type; // 是否有更多信息
//    private String readtimetype; // 读取类型
    private String text;
    // 评论列表
    private List<Comment> list;
}
