package articleProject.service;

import articleProject.dto.CommentDto;
import articleProject.repository.Repository;

public class CommentService {
    private final Repository repository;

    public CommentService(Repository repository) {
        this.repository = repository;
    }

    public void commentAdd(CommentDto commentDto) {
        repository.insertComment(commentDto);
    }
}
