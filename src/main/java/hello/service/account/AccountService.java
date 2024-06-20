package hello.service.account;

import hello.dto.user.AccountDeletionDTO;
import hello.entity.user.ProfileImage;
import hello.repository.db.EmailCodeRepository;
import hello.repository.db.ProfileImageRepository;
import hello.repository.db.UserRepository;
import hello.repository.server.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;
    private final ProfileImageRepository profileImageRepository;
    private final UserRepository userRepository;
    private final FileStore fileStore;

    public void deleteAccountProcess(AccountDeletionDTO accountDeletionDTO) {
        String email = accountDeletionDTO.getEmail();
        String code = accountDeletionDTO.getCode();

        boolean result = emailService.verifyCode(email, code);

        if (result) {
            ProfileImage findImage = profileImageRepository.findByUserId(userRepository.findByEmail(email).getId());
            if (findImage != null) {
                fileStore.deleteProfileImage(findImage.getStoreImageName());
            }

            userRepository.deleteByEmail(email);
            emailCodeRepository.deleteByEmail(email);
        }
    }
}