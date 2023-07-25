package uz.community.javacommunity.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.community.javacommunity.common.exception.AlreadyExistsException;
import uz.community.javacommunity.common.exception.AuthenticationException;
import uz.community.javacommunity.common.exception.RecordNotFoundException;
import uz.community.javacommunity.controller.domain.Article;
import uz.community.javacommunity.controller.domain.Category;
import uz.community.javacommunity.controller.domain.Login;
import uz.community.javacommunity.controller.domain.SubArticle;
import uz.community.javacommunity.controller.repository.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommonSchemaValidator {
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final SubArticleRepository subArticleRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubArticleContentRepository subArticleContentRepository;

    public void validateCategory(UUID id) {
        validateUUID(id, "category");
        if (!categoryRepository.existsById(id)) {
            throwIfNotFound("id", id);
        }
    }

    public void validateArticle(UUID id) {
        validateUUID(id, "article");
        if (!articleRepository.existsById(id)) {
            throwIfNotFound("id", id);
        }
    }

    public void validateSubArticle(UUID id) {
        validateUUID(id, "subArticle");
        if (!subArticleRepository.existsById(id)) {
            throwIfNotFound("id", id);
        }
    }
    public void validateSubArticleContent(UUID id) {
        validateUUID(id, "subArticleContent");
        if (!subArticleContentRepository.existsById(id)) {
            throwIfNotFound("id", id);
        }
    }

    public void validateSubArticleExist(UUID id, String name, UUID articleId, UUID parentId) {
        List<SubArticle> subArticles;
        if (Objects.nonNull(articleId)) {
            validateArticle(articleId);
            subArticles = subArticleRepository.findAllByNameAndArticleId(name, articleId);
        } else {
            validateSubArticle(parentId);
            subArticles = subArticleRepository.findAllByNameAndParentSubArticleId(name, parentId);
        }
        if (Objects.nonNull(id)) {
            subArticles.stream().filter((subArticle) ->
                    subArticle.getId().equals(id)).findFirst().ifPresent(subArticles::remove);
        }
        if (subArticles.size() > 0) {
            throwIfExist("name", name);
        }
    }

    public void validateUsernameExist(String username) {
        if (loginRepository.existsById(username)) {
            throwIfExist("username", username);
        }
    }

    public void validateUUID(UUID id, String propertyName) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException(String.format("%s id cannot be null value", propertyName));
        }
    }

    public void validatePassword(Login login, String password) {
        if (!passwordEncoder.matches(password, login.getPassword())) {
            throw new AuthenticationException();
        }
    }

    public void validateCategoryExistByNameAndParentID(UUID id, String name, UUID parentId) {
        List<Category> categoryList;
        if (Objects.isNull(parentId)) {
            categoryList = categoryRepository.findAllByName(name);
        } else {
            validateCategory(parentId);
            categoryList = categoryRepository.findAllByNameAndParentId(name, parentId);
        }
        if (Objects.nonNull(id)) {
            categoryList.stream().filter((category) ->
                    category.getId().equals(id)).findFirst().ifPresent(categoryList::remove);
        }
        if (categoryList.size() > 0) {
            throwIfExist("name", name);
        }
    }

    public void validateArticleExistByNameAndParentID(UUID id, String name, UUID categoryId) {
        List<Article> articles;
        if (Objects.isNull(categoryId)) {
            articles = articleRepository.findAllByName(name);
        } else {
            validateCategory(categoryId);
            articles = articleRepository.findAllByNameAndCategoryId(name, categoryId);
        }
        if (Objects.nonNull(id)) {
            articles.stream().filter((article) ->
                    article.getId().equals(id)).findFirst().ifPresent(articles::remove);
        }
        if (articles.size() > 0) {
            throwIfExist("name", name);
        }
    }

    public void validateUsernameAndPassword(Login login) {
        String username = login.getUsername();
        String password = login.getPassword();
        validateUsernameExist(username);
        if (!username.matches(".{4,}")) {
            throw new IllegalArgumentException("Username should be at least 4 characters");
        }
        if (!password.matches(".{8,}")){
            throw new IllegalArgumentException("Password should be at least 8 characters");
        }
    }

    public void throwIfExist(String property, Object value) {
        throw new AlreadyExistsException(String.format("Record already exist for %s - %s", property, value));
    }

    public void throwIfNotFound(String property, Object value) {
        throw new RecordNotFoundException(String.format("Record cannot be found for %s - %s", property, value));
    }


}
