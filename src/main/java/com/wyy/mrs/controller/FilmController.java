package com.wyy.mrs.controller;

import com.wyy.mrs.model.entity.Film;
import com.wyy.mrs.service.FilmService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//电影接口
@RequestMapping("/api/film")
public class FilmController {

    @Resource
    private FilmService filmService;

    @PostMapping("")
    //保存电影
    public void save(@RequestBody Film film) {
        filmService.save(film);
    }

    @GetMapping("")
    //列出所有电影
    public List<Film> list(String region, String type) {
        if (region != null && type != null) {
            return filmService.findByRegionAndType(region, type);
        }
        return filmService.findAll();
    }

    @GetMapping("/hot/{limit}")
    //获取热榜电影
    public List<Film> listHots(@PathVariable Integer limit) {
        return filmService.findHots(limit);
    }

    @GetMapping("/name/{name}")
    //搜索电影
    public List<Film> search(@PathVariable String name) {
        return filmService.findLikeName(name);
    }

    @GetMapping("/{id}")
    //根据id查找电影
    public Film findById(@PathVariable String id) {
        return filmService.findById(id);
    }

    @PutMapping("")
    //更新电影
    public void update(@RequestBody Film film) {
        filmService.update(film);
    }

    @DeleteMapping("/{id}")
    //根据id删除电影
    public void deleteById(@PathVariable String id) {
        filmService.deleteById(id);
    }

}
