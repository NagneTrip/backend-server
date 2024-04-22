package com.ssafy.nagne.web;

import static com.ssafy.nagne.domain.Tier.UNRANKED;
import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Gender;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.security.Jwt;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.security.JwtAuthenticationToken;
import com.ssafy.nagne.service.UserService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Value("${file.dir}")
    private String fileDir;

    @PostMapping("/join")
    public ApiResult<JoinResult> join(@Valid @RequestPart JoinRequest request, @RequestPart MultipartFile profileImage)
            throws IOException {
        return success(
                new JoinResult(userService.save(user(request, saveFileAndGetFilePath(profileImage)))
                )
        );
    }

    @PostMapping("/login")
    public ApiResult<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new JwtAuthenticationToken(request.principal(), request.credentials())
        );

        return success(
                new LoginResult(jwt, authentication)
        );
    }

    @GetMapping(path = "/me")
    public ApiResult<UserDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(
                userService.findById(authentication.id())
                        .map(UserDto::new)
                        .orElseThrow(() -> new NotFoundException(
                                "Could not found user for " + authentication.id()))
        );
    }

    private String saveFileAndGetFilePath(MultipartFile profileImage) throws IOException {
        String path = null;

        if (!profileImage.isEmpty()) {
            path = fileDir + profileImage.getOriginalFilename();

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
}
