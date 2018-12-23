package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.Comment;

public interface CommentService {

    public boolean insertComment(Comment comment);

    public boolean deleteComment(Integer commentId);

    public Comment[] getComments(Integer articleId);

    public boolean updateComment(Comment comment);

    public Comment getComment(Integer commentId);
}
