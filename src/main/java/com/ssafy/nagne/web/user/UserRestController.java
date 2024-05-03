package com.ssafy.nagne.web.user;

import static com.ssafy.nagne.domain.Tier.UNRANKED;
import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;
import static org.springframework.util.StringUtils.getFilenameExtension;

import com.ssafy.nagne.domain.Gender;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.UserService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    @Value("${file.dir}")
    private String fileDir;

    private final UserService userService;

    @PostMapping
    public JoinResult join(@Valid @RequestPart JoinRequest request,
                           @RequestPart MultipartFile profileImage) throws IOException {
        return new JoinResult(
                userService.save(user(request, saveFileAndGetFilePath(profileImage, request.username())))
        );
    }

    @GetMapping("/{id}")
    public UserResult findById(@PathVariable Long id) {
        return new UserResult(userService.findById(id));
    }

    @GetMapping("/{id}/detail")
    public UserDetailResult findByIdDetail(@AuthenticationPrincipal JwtAuthentication authentication,
                                           @PathVariable Long id) {
        return new UserDetailResult(userService.findById(authentication.id(), id));
    }

    @GetMapping
    public UserListResult findUsers(@RequestParam(required = false) String keyword) {
        return new UserListResult(userService.findUsers(keyword));
    }

    @GetMapping("/{id}/followers")
    public UserListResult findFollowers(@PathVariable Long id) {
        return new UserListResult(userService.findFollowers(id));
    }

    @GetMapping("/{id}/followings")
    public UserListResult findFollowings(@PathVariable Long id) {
        return new UserListResult(userService.findFollowings(id));
    }

    @PatchMapping("/{id}")
    public Boolean updateInfo(@AuthenticationPrincipal JwtAuthentication authentication,
                              @RequestBody UpdateRequest request) {
        return userService.updateInfo(authentication.id(), user(request));
    }

    @PatchMapping("/{id}/profile-image")
    public Boolean updateProfileImage(@AuthenticationPrincipal JwtAuthentication authentication,
                                      @RequestParam MultipartFile profileImage) throws IOException {
        return userService.updateProfileImage(authentication.id(),
                saveFileAndGetFilePath(profileImage, authentication.username()));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Boolean> delete(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(userService.delete(authentication.id()));
    }

    private String saveFileAndGetFilePath(MultipartFile profileImage, String username) throws IOException {
        String path = null;

        if (!profileImage.isEmpty()) {
            path = fileDir + username + "_profile_image." + getFilenameExtension(profileImage.getOriginalFilename());

            profileImage.transferTo(new File(path));
        }

        return path;
    }

    private User user(JoinRequest request, String imagePath) {
        return User.builder()
                .username(request.username())
                .password(request.password())
                .nickname(request.nickname())
                .phone(request.phone())
                .birth(request.birth())
                .gender(Gender.of(request.gender()))
                .profileImage(imagePath)
                .tier(UNRANKED)
                .createdDate(now())
                .lastModifiedDate(now())
                .build();
    }

    private User user(UpdateRequest request) {
        return User.builder()
                .nickname(request.nickname())
                .phone(request.phone())
                .birth(request.birth())
                .gender(request.gender() == null ? null : Gender.of(request.gender()))
                .build();
    }
}
