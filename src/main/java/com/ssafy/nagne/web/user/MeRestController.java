package com.ssafy.nagne.web.user;

import static com.ssafy.nagne.domain.Tier.UNRANKED;
import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;
import static org.springframework.util.StringUtils.getFilenameExtension;

import com.ssafy.nagne.domain.Gender;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users/me")
@RequiredArgsConstructor
public class MeRestController {

    @Value("${file.dir}")
    private String fileDir;

    private final UserService userService;

    @PostMapping
    public ApiResult<JoinResult> join(@Valid @RequestPart JoinRequest request,
                                      @RequestPart MultipartFile profileImage) throws IOException {
        return success(
                new JoinResult(userService.save(user(request, saveFileAndGetFilePath(profileImage, request.username())))
                )
        );
    }

    @GetMapping
    public ApiResult<UserResult> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                new UserResult(
                        userService.findById(authentication.id())
                                .orElseThrow(() -> new NotFoundException(
                                        "Could not found user for " + authentication.id()))
                )
        );
    }

    @GetMapping("/detail")
    public ApiResult<UserDetailResult> meDetail(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                new UserDetailResult(
                        userService.findById(authentication.id())
                                .orElseThrow(() -> new NotFoundException(
                                        "Could not found user for " + authentication.id()))
                )
        );
    }

    @GetMapping("/followers")
    public ApiResult<UserListResult> findFollowers(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                new UserListResult(userService.findFollowers(authentication.id()))
        );
    }

    @GetMapping("/followings")
    public ApiResult<UserListResult> findFollowings(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                new UserListResult(userService.findFollowings(authentication.id()))
        );
    }

    @PutMapping
    public ApiResult<Boolean> updateInfo(@AuthenticationPrincipal JwtAuthentication authentication,
                                         @RequestBody UpdateRequest request) {
        return success(userService.updateInfo(authentication.id(), user(request)));
    }

    @PutMapping("/profile-image")
    public ApiResult<Boolean> updateProfileImage(@AuthenticationPrincipal JwtAuthentication authentication,
                                                 @RequestParam MultipartFile profileImage) throws IOException {
        return success(userService.updateProfileImage(authentication.id(),
                saveFileAndGetFilePath(profileImage, authentication.username())));
    }

    @DeleteMapping
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
                .email(request.email())
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
                .email(request.email())
                .phone(request.phone())
                .birth(request.birth())
                .gender(request.gender() == null ? null : Gender.of(request.gender()))
                .build();
    }
}
