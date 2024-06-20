package github.maxsplawski.realworld.application.tag;

import github.maxsplawski.realworld.domain.tag.Tag;
import github.maxsplawski.realworld.domain.tag.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getTags() {
        List<String> tagNames = StreamSupport.stream(this.tagRepository.findAll().spliterator(), false)
                .map(Tag::getName)
                .toList();

        Map<String, List<String>> responseBody = new HashMap<>();
        responseBody.put("tags", tagNames);

        return ResponseEntity.ok().body(responseBody);
    }
}
