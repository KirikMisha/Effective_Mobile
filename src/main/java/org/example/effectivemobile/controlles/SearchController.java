package org.example.effectivemobile.controlles;

import org.example.effectivemobile.dto.SearchRequest;
import org.example.effectivemobile.dto.SearchResult;
import org.example.effectivemobile.service.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final SearchServiceImpl searchService;

    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/api/search")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        SearchResult result = searchService.search(request);
        return ResponseEntity.ok(result);
    }
}
