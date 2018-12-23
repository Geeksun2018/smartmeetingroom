package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.CommentMapper;
import cn.hephaestus.smartmeetingroom.model.Comment;
import cn.hephaestus.smartmeetingroom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Override
    public boolean insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    @Override
    public boolean deleteComment(Integer commentId) {
        return commentMapper.deleteComment(commentId);
    }

    @Override
    public Comment[] getComments(Integer articleId) {
        return commentMapper.getComments(articleId);
    }

    @Override
    public boolean updateComment(Comment comment) {
        return commentMapper.updateComment(comment);
    }

    @Override
    public Comment getComment(Integer commentId) {
        return commentMapper.getComment(commentId);
    }
}
