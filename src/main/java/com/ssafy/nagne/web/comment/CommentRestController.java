package com.ssafy.nagne.web.comment;

import com.ssafy.nagne.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    //TODO: CRUD 구현
}
