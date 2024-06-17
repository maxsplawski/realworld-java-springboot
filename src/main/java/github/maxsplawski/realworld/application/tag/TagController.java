package github.maxsplawski.realworld.application.tag;

import github.maxsplawski.realworld.domain.tag.Tag;
import github.maxsplawski.realworld.domain.tag.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getTags() {
        List<Tag> tags = new ArrayList<>();
        this.tagRepository.findAll().forEach(tags::add);
        List<String> tagNames = tags
                .stream()
                .map(Tag::getName)
                .toList();

        Map<String, List<String>> responseBody = new HashMap<>();
        responseBody.put("tags", tagNames);

        return ResponseEntity.ok().body(responseBody);
    }
}
