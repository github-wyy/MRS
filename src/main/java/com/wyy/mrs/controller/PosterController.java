package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Poster;
import com.wyy.mrs.service.PosterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/poster")
public class PosterController {

    @Resource
    private PosterService posterService;

    @PostMapping("")
    public void save(@RequestBody Poster poster) {
        posterService.save(poster);
    }

    @PutMapping("")
    public void update(@RequestBody Poster poster) {
        posterService.update(poster);
    }

    @GetMapping("")
    public List<Poster> list(String status) {
        if (status != null) {
            return posterService.findByStatus(Boolean.parseBoolean(status));
        }
        return posterService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        posterService.deleteById(id);
    }

    @DeleteMapping("")
    public void deleteAll() {
        posterService.deleteAll();
    }

}
