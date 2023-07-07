package com.example.socialmediaapp.service.Impl;

import com.example.socialmediaapp.entities.Post;
import com.example.socialmediaapp.entities.User;
import com.example.socialmediaapp.repository.PostRepository;
import com.example.socialmediaapp.repository.UserRepository;
import com.example.socialmediaapp.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRespository;
    private final UserRepository userRepository;

    @Override
    public List<Post> getAllPosts(){
        List<Post> postList = postRespository.findAll();
        log.info("Post loaded successfully");
        return postList;
    }

    @Override
    public Post addPost(Post post, int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            log.warn("User with id = " + userId + " doesn't exist");
            return null;
        }
        post.setUser(User.builder()
                .userId(userId).build());
        Post savedPost = postRespository.save(post);
        log.info("Post saved successfully");
        return savedPost;
    }

    @Override
    public String likePost(int postId, int userId) {
        Post post = postRespository.findById(postId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (post != null && user != null) {
            post.getUserLikes().add(user);
            postRespository.save(post);
            log.info("Post liked successfully");
            return "Success";
        }
        log.warn("Unexpected error");
        return post == null ? "Post null" : "User null";
    }

    @Override
    public String deletePost(int postId) {
        Post post = postRespository.findById(postId).orElse(null);
        if(post == null){
            log.warn("Post is blank");
            return "Fail";
        }
        postRespository.deleteById(postId);
        log.info("Post deleted successfully");
        return "Success";
    }

}
